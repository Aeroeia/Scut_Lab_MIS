<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Course Statistics</span>
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
            placeholder="Select year">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">Search</el-button>
          <el-button @click="resetSearch">Reset</el-button>
        </el-form-item>
      </el-form>
      
      <!-- Course Information Display -->
      <el-card v-if="showCourseInfo" class="mb-20">
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
        <el-row :gutter="20" class="mt-20">
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
      <div v-loading="loading">
        <el-row :gutter="20" v-if="showCharts">
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
        <el-card class="mt-20" v-if="showClassChart">
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
        <el-card class="mt-20" v-if="showTable">
          <div slot="header" class="clearfix">
            <span>Student Score List</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="handleExport"
              :disabled="!showTable">
              Export Excel
            </el-button>
          </div>
          <el-table
            :data="statisticsData.studentScores"
            border
            style="width: 100%">
            <el-table-column prop="studentId" label="Student ID" width="120" align="center"></el-table-column>
            <el-table-column prop="name" label="Name" width="120" align="center"></el-table-column>
            <el-table-column prop="class" label="Class" width="120" align="center"></el-table-column>
            <el-table-column prop="score" label="Score" width="100" align="center">
              <template slot-scope="scope">
                <span :class="getScoreClass(scope.row.score)">
                  {{ scope.row.score !== null ? scope.row.score : 'Not entered' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="rank" label="Ranking" width="100" align="center"></el-table-column>
            <el-table-column label="Actions" width="120" align="center">
              <template slot-scope="scope">
                <el-button 
                  type="text" 
                  size="small" 
                  @click="viewStudentDetail(scope.row)">
                  Student Details
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
    showCourseInfo() {
      return this.searchForm.courseId && Object.keys(this.currentCourse).length > 0
    },
    showCharts() {
      return this.statisticsData.studentScores && this.statisticsData.studentScores.length > 0
    },
    showTable() {
      return this.statisticsData.studentScores && this.statisticsData.studentScores.length > 0
    }
  },
  mounted() {
    this.fetchCourses()
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
    
    // 获取课程列表
    fetchCourses() {
      this.getCourses({ size: 1000 })
        .then(data => {
          this.courseOptions = data.records || []
          
          // 如果路由传入了课程参数，自动设置
          if (this.$route.query.courseId) {
            this.searchForm.courseId = this.$route.query.courseId
            this.handleCourseChange(this.searchForm.courseId)
          }
        })
        .catch(() => {
          this.$message.error('获取课程列表失败')
        })
    },
    
    // 课程选择变更
    handleCourseChange(courseId) {
      this.currentCourse = this.courseOptions.find(item => item.courseId === courseId) || {}
      if (courseId) {
        this.fetchData()
      }
    },
    
    // 获取课程成绩统计数据
    fetchData() {
      if (!this.searchForm.courseId) {
        this.$message.warning('请选择课程')
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
          this.$message.error('获取课程成绩统计数据失败')
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    // 重置搜索条件
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
    
    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        // 初始化柱状图
        this.initBarChart()
        
        // 初始化饼图
        this.initPieChart()
        
        // 初始化班级对比图
        if (this.statisticsData.classScores && this.statisticsData.classScores.length > 0) {
          this.initClassChart()
        }
      })
    },
    
    // 初始化柱状图
    initBarChart() {
      if (this.barChart) {
        this.barChart.dispose()
      }
      
      const chartDom = this.$refs.barChart
      this.barChart = echarts.init(chartDom)
      
      // 计算成绩区间分布
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
            formatter: '{value} 人'
          }
        },
        series: [
          {
            name: '学生人数',
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
              formatter: '{c} 人'
            }
          }
        ]
      }
      
      this.barChart.setOption(option)
    },
    
    // 初始化饼图
    initPieChart() {
      if (this.pieChart) {
        this.pieChart.dispose()
      }
      
      const chartDom = this.$refs.pieChart
      this.pieChart = echarts.init(chartDom)
      
      // 统计各等级成绩数量
      const levels = [
        { name: '优秀 (90-100分)', value: 0, color: '#67C23A' },
        { name: '良好 (80-89分)', value: 0, color: '#409EFF' },
        { name: '中等 (70-79分)', value: 0, color: '#E6A23C' },
        { name: '及格 (60-69分)', value: 0, color: '#F56C6C' },
        { name: '不及格 (0-59分)', value: 0, color: '#909399' }
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
    
    // 初始化班级对比图
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
          data: ['平均分', '通过率(%)']
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
            name: '分数',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value} 分'
            }
          },
          {
            type: 'value',
            name: '百分比',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value} %'
            }
          }
        ],
        series: [
          {
            name: '平均分',
            type: 'bar',
            data: averageScores,
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '通过率(%)',
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
    
    // 窗口大小变化时重绘图表
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
    
    // 查看学生详情
    viewStudentDetail(student) {
      this.$router.push({
        path: '/statistics/student',
        query: { studentId: student.studentId }
      })
    },
    
    // 导出Excel
    handleExport() {
      // 这里仅为示例，实际导出功能需要调用后端API或使用前端导出库
      this.$message.success('Grade table exported successfully')
    },
    
    // 根据成绩返回不同的Tag类型
    getScoreTagType(score) {
      if (score === null) return 'info'
      if (score >= 90) return 'success'
      if (score >= 75) return 'primary'
      if (score >= 60) return ''
      return 'danger'
    },
    
    // 根据成绩返回等级
    getScoreLevel(score) {
      if (score >= 90) return 'Excellent'
      if (score >= 80) return 'Good'
      if (score >= 70) return 'Medium'
      if (score >= 60) return 'Pass'
      return 'Fail'
    },
    
    // 获取成绩颜色类
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