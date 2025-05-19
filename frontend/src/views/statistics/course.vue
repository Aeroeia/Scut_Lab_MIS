<template>
  <div class="container">
    <el-card class="main-card">
      <div slot="header" class="clearfix">
        <span class="card-title">Course Statistics</span>
      </div>
      
      <!-- Search Criteria -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="Course">
          <el-select 
            v-model="searchForm.courseId" 
            filterable 
            placeholder="Select course"
            style="width: 350px"
            @change="handleCourseChange">
            <el-option
              v-for="item in courseOptions"
              :key="item.courseId"
              :label="`${item.courseId} - ${item.courseName}`"
              :value="item.courseId">
              <span>{{ item.courseId }} - {{ item.courseName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.teacherName }}</span>
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
      
      <!-- Course Information Display -->
      <el-card v-if="showCourseInfo" class="mb-20 info-card">
        <div slot="header" class="clearfix">
          <span>Course Statistics Summary</span>
        </div>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="Course ID">{{ courseInfo.courseId }}</el-descriptions-item>
          <el-descriptions-item label="Course Name">{{ courseInfo.courseName }}</el-descriptions-item>
          <el-descriptions-item label="Teacher">{{ courseInfo.teacherName }}</el-descriptions-item>
          <el-descriptions-item label="Credits">{{ courseInfo.credit }}</el-descriptions-item>
          <el-descriptions-item label="Hours">{{ courseInfo.hours }}</el-descriptions-item>
          <el-descriptions-item label="Semester">{{ courseInfo.semester }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
      
      <!-- Score Statistics Display -->
      <el-card v-if="showScoreInfo" class="mb-20 info-card">
        <div slot="header" class="clearfix">
          <span>Score Statistics</span>
        </div>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Average Score</div>
              <div class="stat-value" :class="getScoreClass(statisticsData.averageScore)">
                {{ statisticsData.averageScore ? statisticsData.averageScore.toFixed(1) : '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Pass Rate</div>
              <div class="stat-value score-pass">
                {{ statisticsData.passRate ? (statisticsData.passRate * 100).toFixed(1) + '%' : '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Excellence Rate</div>
              <div class="stat-value score-excellent">
                {{ statisticsData.excellentRate ? (statisticsData.excellentRate * 100).toFixed(1) + '%' : '-' }}
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mt-20">
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Highest Score</div>
              <div class="stat-value score-excellent">{{ statisticsData.highestScore || '-' }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Lowest Score</div>
              <div class="stat-value" :class="statisticsData.lowestScore && statisticsData.lowestScore < 60 ? 'score-fail' : ''">
                {{ statisticsData.lowestScore || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Student Count</div>
              <div class="stat-value">{{ statisticsData.studentCount || 0 }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- Score Statistics Charts -->
      <div v-loading="loading" class="statistics-container">
        <el-row :gutter="24" v-if="showCharts">
          <!-- Score Distribution Chart -->
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>Score Range Distribution</span>
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
        
        <!-- Class Average Comparison Chart -->
        <el-card class="mt-20 chart-card-large" v-if="showClassChart">
          <div slot="header" class="clearfix">
            <span>Class Average Comparison</span>
            <el-radio-group v-model="displayType" size="small" style="float: right; margin-top: -5px">
              <el-radio-button label="bar">Bar Chart</el-radio-button>
              <el-radio-button label="line">Line Chart</el-radio-button>
            </el-radio-group>
          </div>
          <div class="chart-container chart-container-large">
            <div ref="classChart" class="chart"></div>
          </div>
        </el-card>
        
        <!-- Student Score Table -->
        <el-card class="mt-20 table-card" v-if="showTable">
          <div slot="header" class="clearfix">
            <span>Student Score List</span>
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
            <el-table-column prop="class" label="Class" min-width="120" align="center"></el-table-column>
            <el-table-column prop="score" label="Score" min-width="100" align="center">
              <template slot-scope="scope">
                <span :class="getScoreClass(scope.row.score)">
                  {{ scope.row.score !== null ? scope.row.score : 'Not entered' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="rank" label="Ranking" min-width="100" align="center"></el-table-column>
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
        
        <el-empty v-if="!loading && !showCharts" description="Please select a course to view statistics"></el-empty>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapActions } from 'vuex'

export default {
  name: 'CourseStatistics',
  data() {
    return {
      loading: false,
      courseOptions: [],
      currentCourse: {},
      barChart: null,
      pieChart: null,
      classChart: null,
      displayType: 'bar',
      searchForm: {
        courseId: '',
        year: new Date().getFullYear().toString()
      },
      statisticsData: {
        averageScore: 0,
        studentCount: 0,
        highestScore: 0,
        lowestScore: 0,
        passRate: 0,
        excellentRate: 0,
        studentScores: [],
        classScores: []
      }
    }
  },
  computed: {
    courseInfo() {
      return this.currentCourse || {}
    },
    showCourseInfo() {
      return this.searchForm.courseId && Object.keys(this.currentCourse).length > 0
    },
    showScoreInfo() {
      return this.statisticsData && this.statisticsData.averageScore > 0
    },
    showClassChart() {
      return this.statisticsData.classScores && this.statisticsData.classScores.length > 0
    },
    showCharts() {
      return this.statisticsData.studentScores && this.statisticsData.studentScores.length > 0
    },
    showTable() {
      return this.statisticsData.studentScores && this.statisticsData.studentScores.length > 0
    }
  },
  mounted() {
    this.fetchCourseList()
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
    if (this.classChart) {
      this.classChart.dispose()
    }
  },
  methods: {
    ...mapActions('course', ['getCourses']),
    ...mapActions('statistics', ['getCourseAverageScore']),
    
    // Get course list
    fetchCourseList() {
      this.getCourses({ size: 1000 })
        .then(data => {
          this.courseOptions = data.records || []
          
          // If the course list is not empty, select the first course by default
          if (this.courseOptions.length > 0) {
            this.searchForm.courseId = this.courseOptions[0].courseId
            this.currentCourse = this.courseOptions.find(item => item.courseId === this.searchForm.courseId) || {}
            this.$nextTick(() => {
              this.fetchData()
            })
          }
        })
        .catch(() => {
          this.$message.error('Failed to get course list')
        })
    },
    
    // Course selection changed
    handleCourseChange(courseId) {
      this.currentCourse = this.courseOptions.find(item => item.courseId === courseId) || {}
      if (courseId) {
        this.fetchData()
      }
    },
    
    // Get course score statistics data
    fetchData() {
      if (!this.searchForm.courseId) {
        this.$message.warning('Please select a course')
        return
      }
      
      this.loading = true
      this.getCourseAverageScore({
        courseId: this.searchForm.courseId,
        params: { year: this.searchForm.year }
      })
        .then(data => {
          this.statisticsData = data
          this.$nextTick(() => {
            this.initCharts()
          })
        })
        .catch(() => {
          this.$message.error('Failed to get course statistics data')
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    // Reset search criteria
    resetSearch() {
      this.searchForm = {
        courseId: '',
        year: new Date().getFullYear().toString()
      }
      this.currentCourse = {}
      this.statisticsData = {
        averageScore: 0,
        studentCount: 0,
        highestScore: 0,
        lowestScore: 0,
        passRate: 0,
        excellentRate: 0,
        studentScores: [],
        classScores: []
      }
      if (this.barChart) {
        this.barChart.dispose()
        this.barChart = null
      }
      if (this.pieChart) {
        this.pieChart.dispose()
        this.pieChart = null
      }
      if (this.classChart) {
        this.classChart.dispose()
        this.classChart = null
      }
    },
    
    // Initialize charts
    initCharts() {
      this.$nextTick(() => {
        // Initialize bar chart
        this.initBarChart()
        
        // Initialize pie chart
        this.initPieChart()
        
        // Initialize class comparison chart
        if (this.statisticsData.classScores && this.statisticsData.classScores.length > 0) {
          this.initClassChart()
        }
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
        const score = student.score
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
            name: 'Number of Students',
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
      
      // Count the number of each grade level
      const levels = [
        { name: 'Excellent ', value: 0, color: '#67C23A' },
        { name: 'Good ', value: 0, color: '#409EFF' },
        { name: 'Medium ', value: 0, color: '#E6A23C' },
        { name: 'Pass ', value: 0, color: '#F56C6C' },
        { name: 'Fail ', value: 0, color: '#909399' }
      ]
      
      this.statisticsData.studentScores.forEach(student => {
        const score = student.score
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
    
    // Initialize class comparison chart
    initClassChart() {
      if (this.classChart) {
        this.classChart.dispose()
      }
      
      const chartDom = this.$refs.classChart
      this.classChart = echarts.init(chartDom)
      
      const { classScores } = this.statisticsData
      const classNames = classScores.map(item => item.className)
      const averageScores = classScores.map(item => item.averageScore)
      const passRates = classScores.map(item => (item.passRate * 100).toFixed(1))
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['Average Score', 'Pass Rate (%)']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: classNames,
          axisLabel: {
            interval: 0,
            rotate: 30
          }
        },
        yAxis: [
          {
            type: 'value',
            name: 'Score',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value} points'
            }
          },
          {
            type: 'value',
            name: 'Percentage',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value} %'
            }
          }
        ],
        series: [
          {
            name: 'Average Score',
            type: this.displayType === 'bar' ? 'bar' : 'line',
            data: averageScores,
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: 'Pass Rate (%)',
            type: 'line',
            yAxisIndex: 1,
            data: passRates,
            itemStyle: {
              color: '#67C23A'
            }
          }
        ]
      }
      
      this.classChart.setOption(option)
    },
    
    // Redraw charts when window size changes
    resizeCharts() {
      if (this.barChart) {
        this.barChart.resize()
      }
      if (this.pieChart) {
        this.pieChart.resize()
      }
      if (this.classChart) {
        this.classChart.resize()
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
      // This is just an example, the actual export function needs to call the backend API or use a frontend export library
      this.$message.success('Grade table exported successfully')
    },
    
    // Return different tag types based on score
    getScoreTagType(score) {
      if (score === null) return 'info'
      if (score >= 90) return 'success'
      if (score >= 75) return 'primary'
      if (score >= 60) return ''
      return 'danger'
    },
    
    // Return grade level based on score
    getScoreLevel(score) {
      if (score >= 90) return 'Excellent'
      if (score >= 80) return 'Good'
      if (score >= 70) return 'Medium'
      if (score >= 60) return 'Pass'
      return 'Fail'
    },
    
    // Get score color class
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
.chart-card-large {
  height: 460px;
  transition: all 0.3s;
  border-radius: 6px;
}
.chart-container {
  height: 330px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.chart-container-large {
  height: 370px;
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
.stat-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  text-align: center;
  height: 100%;
  transition: all 0.3s;
}
.stat-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}
.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
}
</style> 