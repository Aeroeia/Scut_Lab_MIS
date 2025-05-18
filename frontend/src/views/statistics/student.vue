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
            placeholder="Select year"
            @change="fetchData">
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
            <el-table-column prop="credit" label="Credits" width="120" align="center"></el-table-column>
            <el-table-column prop="score" label="Score" width="130" align="center">
              <template slot-scope="scope">
                <span :class="getScoreClass(scope.row.score)">
                  {{ scope.row.score !== null ? scope.row.score : 'Not entered' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="semester" label="Semester" width="130" align="center"></el-table-column>
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
      radarChart: null,
      searchForm: {
        studentId: '',
        year: new Date().getFullYear().toString()
      },
      statisticsData: {
        averageScore: 0,
        courseScores: []
      },
      studentInfo: {}
    }
  },
  computed: {
    showStudentInfo() {
      return this.searchForm.studentId && Object.keys(this.studentInfo).length > 0
    },
    showCharts() {
      return this.statisticsData.courseScores && this.statisticsData.courseScores.length > 0
    },
    showTable() {
      return this.statisticsData.courseScores && this.statisticsData.courseScores.length > 0
    }
  },
  mounted() {
    this.fetchStudentList()
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    if (this.barChart) {
      this.barChart.dispose()
    }
    if (this.radarChart) {
      this.radarChart.dispose()
    }
  },
  methods: {
    ...mapActions('student', ['getStudents']),
    ...mapActions('statistics', ['getStudentAverageScore']),
    
    // Get student list
    fetchStudentList() {
      console.log('开始获取学生列表...')
      this.getStudents({ size: 1000 })
        .then(data => {
          console.log('获取到学生数据:', data)
          this.studentOptions = data.records || []
          console.log('学生选项列表:', this.studentOptions)
          
          // 如果学生列表不为空，默认选择第一个学生
          if (this.studentOptions.length > 0) {
            this.searchForm.studentId = this.studentOptions[0].studentId
            this.currentStudent = this.studentOptions.find(item => item.studentId === this.searchForm.studentId) || {}
            this.studentInfo = this.currentStudent;
            console.log('自动选择了第一个学生:', this.currentStudent)
            this.$nextTick(() => {
              this.fetchData()
            })
          }
          
          // If student parameter is passed in route, set it automatically
          if (this.$route.query.studentId) {
            this.searchForm.studentId = this.$route.query.studentId
            this.currentStudent = this.studentOptions.find(item => item.studentId === this.searchForm.studentId) || {}
            this.studentInfo = this.currentStudent;
            if (this.searchForm.studentId) {
              this.fetchData()
            }
          }
        })
        .catch((error) => {
          console.error('获取学生列表失败:', error)
          this.$message.error('Failed to get student list')
        })
    },
    
    // Student selection change
    handleStudentChange(studentId) {
      this.currentStudent = this.studentOptions.find(item => item.studentId === studentId) || {}
      this.studentInfo = this.currentStudent;
      if (studentId) {
        this.fetchData()
      }
    },
    
    // Get student score statistics data
    fetchData() {
      console.log('开始获取成绩数据, 参数:', this.searchForm)
      if (!this.searchForm.studentId) {
        this.$message.warning('Please select a student')
        return
      }
      
      this.loading = true
      
      // 销毁图表但保留变量
      if (this.barChart) {
        this.barChart.dispose()
      }
      if (this.radarChart) {
        this.radarChart.dispose()
      }
      
      this.getStudentAverageScore({
        studentId: this.searchForm.studentId,
        params: { year: this.searchForm.year }
      })
        .then(data => {
          console.log('获取到成绩统计数据:', data)
          this.statisticsData = data
          this.$nextTick(() => {
            this.initCharts()
          })
        })
        .catch((error) => {
          console.error('获取成绩统计数据失败:', error)
          this.$message.error('Failed to get score statistics data')
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    // Reset search criteria
    resetSearch() {
      // 先记住当前的学生选项
      const currentOptions = [...this.studentOptions]
      
      this.searchForm = {
        studentId: '',
        year: new Date().getFullYear().toString()
      }
      this.currentStudent = {}
      this.statisticsData = {
        averageScore: 0,
        courseScores: []
      }
      
      // 恢复学生选项
      this.$nextTick(() => {
        this.studentOptions = currentOptions
      })
      
      if (this.barChart) {
        this.barChart.dispose()
      }
      if (this.radarChart) {
        this.radarChart.dispose()
      }
    },
    
    // Initialize charts
    initCharts() {
      this.$nextTick(() => {
        if (!this.$refs.barChart || !this.$refs.radarChart) {
          return
        }
        
        // Initialize bar chart
        this.initBarChart()
        
        // Initialize radar chart
        this.initRadarChart()
      })
    },
    
    // Initialize bar chart
    initBarChart() {
      if (this.barChart) {
        this.barChart.dispose()
      }
      
      const chartDom = this.$refs.barChart
      if (!chartDom) {
        return;
      }
      
      this.barChart = echarts.init(chartDom)
      
      const { courseScores } = this.statisticsData
      
      // 确保有数据
      if (!courseScores || courseScores.length === 0) {
        this.barChart.setOption({
          title: {
            text: 'No course data available',
            textStyle: {
              color: '#909399',
              fontSize: 14
            },
            left: 'center',
            top: 'middle'
          }
        })
        return;
      }
      
      const courseNames = courseScores.map(item => item.courseName || 'Unknown')
      const scoreData = courseScores.map(item => item.score || 0)
      
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
    
    // Initialize radar chart
    initRadarChart() {
      if (this.radarChart) {
        this.radarChart.dispose()
      }
      
      const chartDom = this.$refs.radarChart
      if (!chartDom) {
        return;
      }
      
      this.radarChart = echarts.init(chartDom)
      
      const { courseScores } = this.statisticsData
      
      // 确保有数据
      if (!courseScores || courseScores.length === 0) {
        this.radarChart.setOption({
          title: {
            text: 'No course data available',
            textStyle: {
              color: '#909399',
              fontSize: 14
            },
            left: 'center',
            top: 'middle'
          }
        })
        return;
      }
      
      // 准备雷达图数据
      const indicator = courseScores.map(item => {
        return {
          name: item.courseName || 'Unknown',
          max: 100
        }
      })
      
      const seriesData = [
        {
          value: courseScores.map(item => item.score || 0),
          name: 'Score'
        }
      ]
      
      const option = {
        tooltip: {
          trigger: 'item'
        },
        radar: {
          indicator: indicator,
          radius: '65%'
        },
        series: [
          {
            name: 'Subject Performance',
            type: 'radar',
            data: seriesData,
            areaStyle: {
              opacity: 0.3
            },
            lineStyle: {
              width: 2
            },
            itemStyle: {
              color: '#409EFF'
            }
          }
        ]
      }
      
      this.radarChart.setOption(option)
    },
    
    // Window size change redraw charts
    resizeCharts() {
      if (this.barChart) {
        this.barChart.resize()
      }
      if (this.radarChart) {
        this.radarChart.resize()
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