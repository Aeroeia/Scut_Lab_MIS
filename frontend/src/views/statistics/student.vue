<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Student Statistics</span>
      </div>
      
      <!-- Search Criteria -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="Student">
          <el-select 
            v-model="searchForm.studentId" 
            filterable 
            placeholder="Select student"
            style="width: 300px"
            @change="handleStudentChange">
            <el-option
              v-for="item in studentOptions"
              :key="item.studentId"
              :label="`${item.studentId} - ${item.name}`"
              :value="item.studentId">
              <span>{{ item.studentId }} - {{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.class }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Academic Year">
          <el-date-picker
            v-model="searchForm.year"
            type="year"
            value-format="yyyy"
            placeholder="Select year">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">Search</el-button>
          <el-button @click="resetSearch">Reset</el-button>
        </el-form-item>
      </el-form>
      
      <!-- Student Information Display -->
      <el-card v-if="showStudentInfo" class="mb-20">
        <div slot="header" class="clearfix">
          <span>Student Information</span>
        </div>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="Student ID">{{ studentInfo.studentId }}</el-descriptions-item>
          <el-descriptions-item label="Name">{{ studentInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="Gender">{{ studentInfo.gender }}</el-descriptions-item>
          <el-descriptions-item label="Class">{{ studentInfo.class }}</el-descriptions-item>
          <el-descriptions-item label="Enrollment Year">{{ studentInfo.enrollmentYear }}</el-descriptions-item>
          <el-descriptions-item label="Age at Enrollment">{{ studentInfo.ageAtEnrollment }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
      
      <!-- Score Statistics Display -->
      <el-card v-if="showScoreInfo" class="mb-20">
        <div slot="header" class="clearfix">
          <span>Score Statistics</span>
        </div>
        <el-row :gutter="20">
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
              <div class="stat-title">Highest Score</div>
              <div class="stat-value score-excellent">
                {{ statisticsData.highestScore || '-' }}
              </div>
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
        </el-row>
        <el-row :gutter="20" class="mt-20">
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Credits Obtained</div>
              <div class="stat-value">{{ statisticsData.totalCredits || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Courses Enrolled</div>
              <div class="stat-value">{{ statisticsData.courseCount || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Class Ranking</div>
              <div class="stat-value">
                {{ statisticsData.classRank ? `${statisticsData.classRank}/${statisticsData.classTotal}` : '-' }}
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- Score Statistics Charts -->
      <div v-loading="loading">
        <el-row :gutter="20" v-if="showCharts">
          <!-- Score Distribution Chart -->
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
          
          <!-- Course Score Radar Chart -->
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>Subject Performance</span>
              </div>
              <div class="chart-container">
                <div ref="radarChart" class="chart"></div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <!-- Course Score List -->
        <el-card class="mt-20" v-if="showTable">
          <div slot="header" class="clearfix">
            <span>Course Score List</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="handleExport"
              :disabled="!showTable">
              Export Excel
            </el-button>
          </div>
          <el-table
            :data="statisticsData.courseScores"
            border
            style="width: 100%">
            <el-table-column prop="courseId" label="Course ID" width="120" align="center"></el-table-column>
            <el-table-column prop="courseName" label="Course Name" min-width="150" align="center"></el-table-column>
            <el-table-column prop="teacherName" label="Teacher" width="120" align="center"></el-table-column>
            <el-table-column prop="credit" label="Credits" width="80" align="center"></el-table-column>
            <el-table-column prop="score" label="Score" width="100" align="center">
              <template slot-scope="scope">
                <span :class="getScoreClass(scope.row.score)">
                  {{ scope.row.score !== null ? scope.row.score : 'Not entered' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="semester" label="Semester" width="180" align="center"></el-table-column>
            <el-table-column label="Actions" width="120" align="center">
              <template slot-scope="scope">
                <el-button 
                  type="text" 
                  size="small" 
                  @click="viewCourseDetail(scope.row)">
                  Course Details
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        
        <el-empty v-if="!loading && !showCharts" description="Please select a student to view statistics"></el-empty>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapActions } from 'vuex'

export default {
  name: 'StudentStatistics',
  data() {
    return {
      loading: false,
      studentOptions: [],
      currentStudent: {},
      barChart: null,
      pieChart: null,
      searchForm: {
        studentId: '',
        year: new Date().getFullYear().toString()
      },
      statisticsData: {
        averageScore: 0,
        courseScores: []
      }
    }
  },
  computed: {
    showStudentInfo() {
      return this.searchForm.studentId && Object.keys(this.currentStudent).length > 0
    },
    showCharts() {
      return this.statisticsData.courseScores && this.statisticsData.courseScores.length > 0
    },
    showTable() {
      return this.statisticsData.courseScores && this.statisticsData.courseScores.length > 0
    }
  },
  mounted() {
    this.fetchStudents()
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
    ...mapActions('statistics', ['getStudentAverageScore']),
    
    // Get student list
    fetchStudents() {
      this.getStudents({ size: 1000 })
        .then(data => {
          this.studentOptions = data.records || []
          
          // If student parameter is passed in route, set it automatically
          if (this.$route.query.studentId) {
            this.searchForm.studentId = this.$route.query.studentId
            this.currentStudent = this.studentOptions.find(item => item.studentId === this.searchForm.studentId) || {}
            if (this.searchForm.studentId) {
              this.fetchData()
            }
          }
        })
        .catch(() => {
          this.$message.error('Failed to get student list')
        })
    },
    
    // Student selection change
    handleStudentChange(studentId) {
      this.currentStudent = this.studentOptions.find(item => item.studentId === studentId) || {}
      if (studentId) {
        this.fetchData()
      }
    },
    
    // Get student score statistics data
    fetchData() {
      if (!this.searchForm.studentId) {
        this.$message.warning('Please select a student')
        return
      }
      
      this.loading = true
      this.getStudentAverageScore({
        studentId: this.searchForm.studentId,
        params: { year: this.searchForm.year }
      })
        .then(data => {
          this.statisticsData = data
          this.$nextTick(() => {
            this.initCharts()
          })
        })
        .catch(() => {
          this.$message.error('Failed to get score statistics data')
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    // Reset search criteria
    resetSearch() {
      this.searchForm = {
        studentId: '',
        year: new Date().getFullYear().toString()
      }
      this.currentStudent = {}
      this.statisticsData = {
        averageScore: 0,
        courseScores: []
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
      
      const { courseScores } = this.statisticsData
      const courseNames = courseScores.map(item => item.courseName)
      const scoreData = courseScores.map(item => item.score)
      
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
          data: courseNames,
          axisLabel: {
            interval: 0,
            rotate: 30
          }
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 100,
          axisLabel: {
            formatter: '{value} points'
          }
        },
        series: [
          {
            name: 'Score',
            type: 'bar',
            data: scoreData,
            itemStyle: {
              color: params => {
                const score = params.value
                if (score >= 90) return '#67C23A'
                if (score >= 80) return '#409EFF'
                if (score >= 70) return '#E6A23C'
                if (score >= 60) return '#F56C6C'
                return '#909399'
              }
            },
            label: {
              show: true,
              position: 'top',
              formatter: '{c} points'
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
      
      const { courseScores } = this.statisticsData
      
      // Count grades
      const levels = [
        { name: 'Excellent (90-100 points)', value: 0, color: '#67C23A' },
        { name: 'Good (80-89 points)', value: 0, color: '#409EFF' },
        { name: 'Medium (70-79 points)', value: 0, color: '#E6A23C' },
        { name: 'Pass (60-69 points)', value: 0, color: '#F56C6C' },
        { name: 'Fail (0-59 points)', value: 0, color: '#909399' }
      ]
      
      courseScores.forEach(item => {
        const score = item.score
        if (score >= 90) levels[0].value++
        else if (score >= 80) levels[1].value++
        else if (score >= 70) levels[2].value++
        else if (score >= 60) levels[3].value++
        else levels[4].value++
      })
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          data: levels.map(item => item.name)
        },
        series: [
          {
            name: 'Score Level',
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
    
    // Window size change redraw charts
    resizeCharts() {
      if (this.barChart) {
        this.barChart.resize()
      }
      if (this.pieChart) {
        this.pieChart.resize()
      }
    },
    
    // Return different Tag type based on score
    getScoreTagType(score) {
      if (score >= 90) return 'success'
      if (score >= 75) return 'primary'
      if (score >= 60) return ''
      return 'danger'
    },
    
    // Return grade based on score
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
.mb-20 {
  margin-bottom: 20px;
}
.mt-20 {
  margin-top: 20px;
}
.chart-card {
  height: 400px;
}
.chart-container {
  height: 310px;
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
</style> 