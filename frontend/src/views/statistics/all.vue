<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>School-wide Statistics</span>
      </div>
      
      <!-- Search Criteria -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="Academic Year">
          <el-date-picker
            v-model="searchForm.year"
            type="year"
            value-format="yyyy"
            placeholder="Select year">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="Semester">
          <el-select v-model="searchForm.semester" placeholder="Select semester" clearable>
            <el-option label="First Semester" value="1"></el-option>
            <el-option label="Second Semester" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">Search</el-button>
          <el-button @click="resetSearch">Reset</el-button>
        </el-form-item>
      </el-form>
      
      <!-- Statistics Display -->
      <el-card v-if="showStatistics" class="mb-20">
        <div slot="header" class="clearfix">
          <span>School-wide Statistics Summary</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">School Average Score</div>
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
              <div class="stat-title">Students Count</div>
              <div class="stat-value">{{ statisticsData.studentCount || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Courses Count</div>
              <div class="stat-value">{{ statisticsData.courseCount || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-title">Total Score Records</div>
              <div class="stat-value">{{ statisticsData.scoreCount || 0 }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- Score Statistics Charts -->
      <div v-loading="loading">
        <el-row :gutter="20" v-if="showCharts">
          <!-- Score Range Distribution -->
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>Score Range Distribution</span>
              </div>
              <div class="chart-container">
                <div ref="scoreRangeChart" class="chart"></div>
              </div>
            </el-card>
          </el-col>
          
          <!-- Grade Level Distribution -->
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>Grade Level Distribution</span>
              </div>
              <div class="chart-container">
                <div ref="levelPieChart" class="chart"></div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <!-- Class Comparison -->
        <el-card class="mt-20" v-if="showCharts">
          <div slot="header" class="clearfix">
            <span>Class Average Score Comparison</span>
            <el-radio-group v-model="displayType" size="small" style="float: right; margin-top: -5px">
              <el-radio-button label="bar">Bar Chart</el-radio-button>
              <el-radio-button label="line">Line Chart</el-radio-button>
            </el-radio-group>
          </div>
          <div class="chart-container chart-container-large">
            <div ref="classCompareChart" class="chart"></div>
          </div>
        </el-card>
        
        <!-- Course Score TOP10 -->
        <el-row :gutter="20" class="mt-20" v-if="showCharts">
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>TOP10 Courses with Highest Average Score</span>
              </div>
              <div class="chart-container">
                <div ref="topCoursesChart" class="chart"></div>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>TOP10 Courses with Lowest Average Score</span>
              </div>
              <div class="chart-container">
                <div ref="bottomCoursesChart" class="chart"></div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-empty v-if="!loading && !showCharts" description="No statistics data available"></el-empty>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapActions } from 'vuex'

export default {
  name: 'AllStatistics',
  data() {
    return {
      loading: false,
      displayType: 'bar',
      scoreRangeChart: null,
      levelPieChart: null,
      classCompareChart: null,
      topCoursesChart: null,
      bottomCoursesChart: null,
      searchForm: {
        year: new Date().getFullYear().toString(),
        semester: ''
      },
      statisticsData: {
        averageScore: 0,
        passRate: 0,
        excellentRate: 0,
        studentCount: 0,
        courseCount: 0,
        scoreCount: 0,
        scoreRangeDistribution: [],
        levelDistribution: [],
        classAverages: [],
        topCourses: [],
        bottomCourses: []
      }
    }
  },
  computed: {
    showStatistics() {
      return this.statisticsData.studentCount > 0
    },
    showCharts() {
      return this.statisticsData.studentCount > 0
    }
  },
  mounted() {
    // 默认选择第一个学期
    this.searchForm.semester = "1"
    this.fetchData()
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    this.disposeCharts()
  },
  watch: {
    displayType() {
      this.$nextTick(() => {
        this.initClassCompareChart()
      })
    }
  },
  methods: {
    ...mapActions('statistics', ['getAllStatistics']),
    
    // 获取全校成绩统计数据
    fetchData() {
      this.loading = true
      
      this.getAllStatistics({
        year: this.searchForm.year,
        semester: this.searchForm.semester || undefined
      })
        .then(data => {
          this.statisticsData = data
          this.$nextTick(() => {
            this.initCharts()
          })
        })
        .catch(() => {
          this.$message.error('获取统计数据失败')
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    // 重置搜索条件
    resetSearch() {
      this.searchForm = {
        year: new Date().getFullYear().toString(),
        semester: ''
      }
      this.fetchData()
    },
    
    // 初始化所有图表
    initCharts() {
      this.$nextTick(() => {
        this.initScoreRangeChart()
        this.initLevelPieChart()
        this.initClassCompareChart()
        this.initTopCoursesChart()
        this.initBottomCoursesChart()
      })
    },
    
    // 初始化分数段分布图
    initScoreRangeChart() {
      if (this.scoreRangeChart) {
        this.scoreRangeChart.dispose()
      }
      
      const chartDom = this.$refs.scoreRangeChart
      this.scoreRangeChart = echarts.init(chartDom)
      
      const { scoreRangeDistribution } = this.statisticsData
      
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
          data: scoreRangeDistribution.map(item => item.range),
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
            data: scoreRangeDistribution.map(item => item.count),
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
      
      this.scoreRangeChart.setOption(option)
    },
    
    // 初始化等级分布饼图
    initLevelPieChart() {
      if (this.levelPieChart) {
        this.levelPieChart.dispose()
      }
      
      const chartDom = this.$refs.levelPieChart
      this.levelPieChart = echarts.init(chartDom)
      
      const { levelDistribution } = this.statisticsData
      
      const colors = ['#67C23A', '#409EFF', '#E6A23C', '#F56C6C', '#909399']
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}人 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          data: levelDistribution.map(item => item.level)
        },
        series: [
          {
            name: '成绩等级',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
              color: (params) => colors[params.dataIndex]
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
            data: levelDistribution.map(item => ({
              name: item.level,
              value: item.count
            }))
          }
        ]
      }
      
      this.levelPieChart.setOption(option)
    },
    
    // 初始化班级对比图
    initClassCompareChart() {
      if (this.classCompareChart) {
        this.classCompareChart.dispose()
      }
      
      const chartDom = this.$refs.classCompareChart
      this.classCompareChart = echarts.init(chartDom)
      
      const { classAverages } = this.statisticsData
      
      // 按平均分排序
      const sortedClasses = [...classAverages].sort((a, b) => b.averageScore - a.averageScore)
      
      const classNames = sortedClasses.map(item => item.className)
      const averageScores = sortedClasses.map(item => item.averageScore.toFixed(1))
      const passRates = sortedClasses.map(item => (item.passRate * 100).toFixed(1))
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: this.displayType === 'line' ? 'line' : 'shadow'
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
            type: this.displayType,
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
      
      this.classCompareChart.setOption(option)
    },
    
    // 初始化TOP10课程图表
    initTopCoursesChart() {
      if (this.topCoursesChart) {
        this.topCoursesChart.dispose()
      }
      
      const chartDom = this.$refs.topCoursesChart
      this.topCoursesChart = echarts.init(chartDom)
      
      const { topCourses } = this.statisticsData
      
      // 数据格式化
      const courseNames = topCourses.map(item => item.courseName)
      const averageScores = topCourses.map(item => item.averageScore.toFixed(1))
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: function(params) {
            return `${params[0].name}<br/>${params[0].seriesName}: ${params[0].value} 分`
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          min: 0,
          max: 100,
          axisLabel: {
            formatter: '{value} 分'
          }
        },
        yAxis: {
          type: 'category',
          data: courseNames,
          axisLabel: {
            interval: 0
          }
        },
        series: [
          {
            name: '平均分',
            type: 'bar',
            data: averageScores,
            itemStyle: {
              color: '#67C23A'
            },
            label: {
              show: true,
              position: 'right',
              formatter: '{c} 分'
            }
          }
        ]
      }
      
      this.topCoursesChart.setOption(option)
    },
    
    // 初始化后10课程图表
    initBottomCoursesChart() {
      if (this.bottomCoursesChart) {
        this.bottomCoursesChart.dispose()
      }
      
      const chartDom = this.$refs.bottomCoursesChart
      this.bottomCoursesChart = echarts.init(chartDom)
      
      const { bottomCourses } = this.statisticsData
      
      // 数据格式化
      const courseNames = bottomCourses.map(item => item.courseName)
      const averageScores = bottomCourses.map(item => item.averageScore.toFixed(1))
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: function(params) {
            return `${params[0].name}<br/>${params[0].seriesName}: ${params[0].value} 分`
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          min: 0,
          max: 100,
          axisLabel: {
            formatter: '{value} 分'
          }
        },
        yAxis: {
          type: 'category',
          data: courseNames,
          axisLabel: {
            interval: 0
          }
        },
        series: [
          {
            name: '平均分',
            type: 'bar',
            data: averageScores,
            itemStyle: {
              color: '#F56C6C'
            },
            label: {
              show: true,
              position: 'right',
              formatter: '{c} 分'
            }
          }
        ]
      }
      
      this.bottomCoursesChart.setOption(option)
    },
    
    // 窗口大小变化时重绘图表
    resizeCharts() {
      if (this.scoreRangeChart) {
        this.scoreRangeChart.resize()
      }
      if (this.levelPieChart) {
        this.levelPieChart.resize()
      }
      if (this.classCompareChart) {
        this.classCompareChart.resize()
      }
      if (this.topCoursesChart) {
        this.topCoursesChart.resize()
      }
      if (this.bottomCoursesChart) {
        this.bottomCoursesChart.resize()
      }
    },
    
    // 销毁所有图表
    disposeCharts() {
      if (this.scoreRangeChart) {
        this.scoreRangeChart.dispose()
        this.scoreRangeChart = null
      }
      if (this.levelPieChart) {
        this.levelPieChart.dispose()
        this.levelPieChart = null
      }
      if (this.classCompareChart) {
        this.classCompareChart.dispose()
        this.classCompareChart = null
      }
      if (this.topCoursesChart) {
        this.topCoursesChart.dispose()
        this.topCoursesChart = null
      }
      if (this.bottomCoursesChart) {
        this.bottomCoursesChart.dispose()
        this.bottomCoursesChart = null
      }
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
.chart-container-large {
  height: 400px;
}
.chart {
  width: 100%;
  height: 100%;
}
.stat-card {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.stat-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
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