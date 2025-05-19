<template>
  <div class="container">
    <el-card class="main-card">
      <div slot="header" class="clearfix">
        <span class="card-title">Class Statistics</span>
      </div>
      
      <!-- Search Criteria -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="Class">
          <el-select 
            v-model="searchForm.className" 
            filterable 
            placeholder="Select class"
            style="width: 250px"
            @change="handleClassChange">
            <el-option
              v-for="item in classOptions"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Academic Year">
          <el-date-picker
            v-model="searchForm.year"
            type="year"
            value-format="yyyy"
            placeholder="Select year"
            @change="fetchData">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="fetchData">Search</el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch">Reset</el-button>
        </el-form-item>
      </el-form>
      
      <!-- Class Information Display -->
      <el-card v-if="showClassInfo" class="mb-20 info-card">
        <div slot="header" class="clearfix">
          <span>Class Statistics Summary</span>
        </div>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="Class Name">{{ searchForm.className }}</el-descriptions-item>
          <el-descriptions-item label="Student Count">{{ statisticsData.studentCount || 0 }} students</el-descriptions-item>
          <el-descriptions-item label="Average Score">
            <span class="average-score" :class="getScoreClass(statisticsData.averageScore)">
              {{ statisticsData.averageScore ? statisticsData.averageScore.toFixed(1) : '-' }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="Highest Score">
            <span class="score-excellent">{{ statisticsData.highestScore || '-' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Lowest Score">
            <span class="score-fail">{{ statisticsData.lowestScore || '-' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Excellence Rate">
            {{ statisticsData.excellentRate ? (statisticsData.excellentRate * 100).toFixed(1) + '%' : '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
      
      <!-- Score Statistics Charts -->
      <div v-loading="loading" class="statistics-container">
        <el-row :gutter="24" v-if="showCharts">
          <!-- Score Distribution Bar Chart -->
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>Score Distribution</span>
              </div>
              <div class="chart-container">
                <div ref="barChart" class="chart"></div>
              </div>
            </el-card>
          </el-col>
          
          <!-- Grade Level Pie Chart -->
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>Grade Level Distribution</span>
              </div>
              <div class="chart-container">
                <div ref="pieChart" class="chart"></div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <!-- Student Score Table -->
        <el-card class="mt-20 table-card" v-if="showTable">
          <div slot="header" class="clearfix">
            <span>Class Student Score List</span>
            <el-tooltip content="Export to Excel file" placement="top">
              <el-button
                style="float: right; padding: 3px 0"
                type="text"
                icon="el-icon-download"
                @click="handleExport"
                :disabled="!showTable">
                Export Excel
              </el-button>
            </el-tooltip>
          </div>
          <div class="table-hint">
            <i class="el-icon-info"></i> Click on a row to view student's detailed statistics
          </div>
          <el-table
            :data="statisticsData.studentScores"
            border
            style="width: 100%"
            @row-click="viewStudentDetail"
            class="student-table">
            <el-table-column prop="studentId" label="Student ID" width="120" align="center"></el-table-column>
            <el-table-column prop="name" label="Name" width="120" align="center"></el-table-column>
            <el-table-column prop="gender" label="Gender" width="80" align="center"></el-table-column>
            <el-table-column prop="averageScore" label="Average Score" min-width="120" align="center">
              <template slot-scope="scope">
                <span :class="getScoreClass(scope.row.averageScore)">
                  {{ scope.row.averageScore ? scope.row.averageScore.toFixed(1) : '-' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="rank" label="Class Ranking" min-width="120" align="center"></el-table-column>
            <el-table-column label="Actions" align="center" width="120">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="small"
                  icon="el-icon-view"
                  @click.stop="viewStudentDetail(scope.row)">
                  View Details
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        
        <el-empty v-if="!loading && !showCharts" description="Please select a class to view statistics"></el-empty>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapActions } from 'vuex'

export default {
  name: 'ClassStatistics',
  data() {
    return {
      loading: false,
      classOptions: [],
      barChart: null,
      pieChart: null,
      searchForm: {
        className: '',
        year: new Date().getFullYear().toString()
      },
      statisticsData: {
        averageScore: 0,
        studentCount: 0,
        highestScore: 0,
        lowestScore: 0,
        excellentRate: 0,
        studentScores: []
      }
    }
  },
  computed: {
    showClassInfo() {
      return this.searchForm.className && this.statisticsData.studentCount > 0
    },
    showCharts() {
      return this.statisticsData.studentScores && this.statisticsData.studentScores.length > 0
    },
    showTable() {
      return this.statisticsData.studentScores && this.statisticsData.studentScores.length > 0
    }
  },
  mounted() {
    this.fetchClassList()
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    if (this.barChart) {
      this.barChart.dispose()
    }
    if (this.pieChart) {
      this.pieChart.dispose()
    }
  },
  methods: {
    ...mapActions('student', ['getStudents']),
    ...mapActions('statistics', ['getClassAverageScore']),
    
    // Get class list
    fetchClassList() {
      this.getStudents({ size: 1000 })
        .then(data => {
          // Extract unique class names from student data
          const classList = [...new Set(data.records.map(item => item.class))].filter(Boolean)
          this.classOptions = classList.sort()
          
          // 如果班级列表不为空，默认选择第一个班级
          if (this.classOptions.length > 0) {
            this.searchForm.className = this.classOptions[0]
            this.$nextTick(() => {
              this.fetchData()
            })
          }
          
          // If class parameter is passed in route, set it automatically
          if (this.$route.query.className) {
            this.searchForm.className = this.$route.query.className
            this.fetchData()
          }
        })
        .catch(() => {
          this.$message.error('Failed to get class list')
        })
    },
    
    // Class selection change
    handleClassChange(className) {
      if (className) {
        this.fetchData()
      }
    },
    
    // Get class statistics data
    fetchData() {
      if (!this.searchForm.className) {
        this.$message.warning('Please select a class')
        return
      }
      
      this.loading = true
      this.getClassAverageScore({
        className: this.searchForm.className,
        params: { year: this.searchForm.year }
      })
        .then(data => {
          this.statisticsData = data
          this.$nextTick(() => {
            this.initCharts()
          })
        })
        .catch(() => {
          this.$message.error('Failed to get class statistics data')
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    // Reset search criteria
    resetSearch() {
      this.searchForm = {
        className: '',
        year: new Date().getFullYear().toString()
      }
      this.statisticsData = {
        averageScore: 0,
        studentCount: 0,
        highestScore: 0,
        lowestScore: 0,
        excellentRate: 0,
        studentScores: []
      }
      if (this.barChart) {
        this.barChart.dispose()
        this.barChart = null
      }
      if (this.pieChart) {
        this.pieChart.dispose()
        this.pieChart = null
      }
    },
    
    // Initialize charts
    initCharts() {
      this.$nextTick(() => {
        // Initialize bar chart
        this.initBarChart()
        
        // Initialize pie chart
        this.initPieChart()
      })
    },
    
    // Initialize bar chart
    initBarChart() {
      if (this.barChart) {
        this.barChart.dispose()
      }
      
      const chartDom = this.$refs.barChart
      this.barChart = echarts.init(chartDom)
      
      // Calculate score range distribution
      const scoreRanges = [
        { range: '90-100', count: 0 },
        { range: '80-89', count: 0 },
        { range: '70-79', count: 0 },
        { range: '60-69', count: 0 },
        { range: '0-59', count: 0 }
      ]
      
      this.statisticsData.studentScores.forEach(student => {
        const score = student.averageScore
        if (score >= 90) scoreRanges[0].count++
        else if (score >= 80) scoreRanges[1].count++
        else if (score >= 70) scoreRanges[2].count++
        else if (score >= 60) scoreRanges[3].count++
        else scoreRanges[4].count++
      })
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: scoreRanges.map(item => item.range),
          axisLabel: {
            interval: 0
          }
        },
        yAxis: {
          type: 'value',
          minInterval: 1,
          axisLabel: {
            formatter: '{value} students'
          }
        },
        series: [
          {
            name: 'Student Count',
            type: 'bar',
            data: scoreRanges.map(item => item.count),
            itemStyle: {
              color: (params) => {
                const colors = ['#67C23A', '#409EFF', '#E6A23C', '#F56C6C', '#909399']
                return colors[params.dataIndex]
              }
            },
            label: {
              show: true,
              position: 'top',
              formatter: '{c} students'
            }
          }
        ]
      }
      
      this.barChart.setOption(option)
    },
    
    // Initialize pie chart
    initPieChart() {
      if (this.pieChart) {
        this.pieChart.dispose()
      }
      
      const chartDom = this.$refs.pieChart
      this.pieChart = echarts.init(chartDom)
      
      // Count grades
      const levels = [
        { name: 'Excellent (90-100 points)', value: 0, color: '#67C23A' },
        { name: 'Good (80-89 points)', value: 0, color: '#409EFF' },
        { name: 'Medium (70-79 points)', value: 0, color: '#E6A23C' },
        { name: 'Pass (60-69 points)', value: 0, color: '#F56C6C' },
        { name: 'Fail (0-59 points)', value: 0, color: '#909399' }
      ]
      
      this.statisticsData.studentScores.forEach(student => {
        const score = student.averageScore
        if (score >= 90) levels[0].value++
        else if (score >= 80) levels[1].value++
        else if (score >= 70) levels[2].value++
        else if (score >= 60) levels[3].value++
        else levels[4].value++
      })
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} students ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          data: levels.map(item => item.name)
        },
        series: [
          {
            name: 'Grade Level',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
              color: params => {
                return levels[params.dataIndex].color
              }
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: levels.map(item => ({
              name: item.name,
              value: item.value
            }))
          }
        ]
      }
      
      this.pieChart.setOption(option)
    },
    
    // Resize charts when window size changes
    resizeCharts() {
      if (this.barChart) {
        this.barChart.resize()
      }
      if (this.pieChart) {
        this.pieChart.resize()
      }
    },
    
    // View student details
    viewStudentDetail(student) {
      this.$router.push({
        path: '/statistics/student',
        query: { studentId: student.studentId }
      })
    },
    
    // Export Excel
    handleExport() {
      // This is just an example, actual export functionality needs to call backend API or use frontend export library
      this.$message.success('Export successful')
    },
    
    // Get score level
    getScoreLevel(score) {
      if (score >= 90) return 'Excellent'
      if (score >= 80) return 'Good'
      if (score >= 70) return 'Medium'
      if (score >= 60) return 'Pass'
      return 'Fail'
    },
    
    // Get score class
    getScoreClass(score) {
      if (!score) return ''
      if (score >= 90) return 'score-excellent'
      if (score >= 80) return 'score-good'
      if (score >= 70) return 'score-medium'
      if (score >= 60) return 'score-pass'
      return 'score-fail'
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20px;
}
.main-card {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}
.card-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}
.mb-20 {
  margin-bottom: 20px;
}
.mt-20 {
  margin-top: 20px;
}
.info-card {
  background-color: #f9fafb;
  border: 1px solid #ebeef5;
  transition: all 0.3s;
}
.info-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.chart-card {
  height: 420px;
  transition: all 0.3s;
  border-radius: 6px;
  margin-bottom: 20px;
}
.chart-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}
.chart-container {
  height: 330px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.chart {
  width: 100%;
  height: 100%;
}
.average-score {
  font-size: 18px;
  font-weight: bold;
}
.score-excellent {
  color: #67C23A;
}
.score-good {
  color: #409EFF;
}
.score-medium {
  color: #E6A23C;
}
.score-pass {
  color: #F56C6C;
}
.score-fail {
  color: #909399;
}
.statistics-container {
  margin-top: 20px;
}
.table-card {
  border-radius: 6px;
  width: 100%;
  margin-bottom: 20px;
}
.table-hint {
  color: #909399;
  font-size: 13px;
  margin-bottom: 10px;
}
.student-table {
  border-radius: 4px;
  overflow: hidden;
  width: 100%;
}
.student-table /deep/ .el-table__row {
  cursor: pointer;
  transition: all 0.3s;
}
.student-table /deep/ .el-table__row:hover {
  background-color: #f0f9ff;
}
</style> 