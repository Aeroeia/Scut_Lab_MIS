<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>学生成绩统计</span>
      </div>
      
      <!-- 查询条件 -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="学生">
          <el-select 
            v-model="searchForm.studentId" 
            filterable 
            placeholder="请选择学生"
            style="width: 250px"
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
        <el-form-item label="学年">
          <el-date-picker
            v-model="searchForm.year"
            type="year"
            value-format="yyyy"
            placeholder="选择年份">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 学生信息展示 -->
      <el-card v-if="showStudentInfo" class="mb-20">
        <div slot="header" class="clearfix">
          <span>学生基本信息</span>
        </div>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="学号">{{ currentStudent.studentId }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ currentStudent.name }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ currentStudent.gender }}</el-descriptions-item>
          <el-descriptions-item label="班级">{{ currentStudent.class }}</el-descriptions-item>
          <el-descriptions-item label="入学年份">{{ currentStudent.enrollmentYear }}</el-descriptions-item>
          <el-descriptions-item label="平均成绩">
            <span class="average-score" :class="getScoreClass(statisticsData.averageScore)">
              {{ statisticsData.averageScore ? statisticsData.averageScore.toFixed(1) : '-' }}
            </span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
      
      <!-- 成绩统计图表 -->
      <div v-loading="loading">
        <el-row :gutter="20" v-if="showCharts">
          <!-- 成绩柱状图 -->
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>各课程成绩分布</span>
              </div>
              <div class="chart-container">
                <div ref="barChart" class="chart"></div>
              </div>
            </el-card>
          </el-col>
          
          <!-- 成绩等级饼图 -->
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>成绩等级分布</span>
              </div>
              <div class="chart-container">
                <div ref="pieChart" class="chart"></div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <!-- 成绩表格 -->
        <el-card class="mt-20" v-if="showTable">
          <div slot="header" class="clearfix">
            <span>学生成绩详情</span>
          </div>
          <el-table
            :data="statisticsData.courseScores"
            border
            style="width: 100%">
            <el-table-column prop="courseId" label="课程编号" width="120" align="center"></el-table-column>
            <el-table-column prop="courseName" label="课程名称" min-width="180" align="center"></el-table-column>
            <el-table-column prop="credit" label="学分" width="80" align="center"></el-table-column>
            <el-table-column prop="teacherName" label="授课教师" width="120" align="center"></el-table-column>
            <el-table-column prop="score" label="成绩" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getScoreTagType(scope.row.score)">{{ scope.row.score }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="level" label="等级" width="100" align="center">
              <template slot-scope="scope">
                {{ getScoreLevel(scope.row.score) }}
              </template>
            </el-table-column>
            <el-table-column prop="semester" label="学期" width="150" align="center"></el-table-column>
          </el-table>
        </el-card>
        
        <el-empty v-if="!loading && !showCharts" description="请选择学生查看成绩统计"></el-empty>
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
    
    // 获取学生列表
    fetchStudents() {
      this.getStudents({ size: 1000 })
        .then(data => {
          this.studentOptions = data.records || []
          
          // 如果路由传入了学生参数，自动设置
          if (this.$route.query.studentId) {
            this.searchForm.studentId = this.$route.query.studentId
            this.currentStudent = this.studentOptions.find(item => item.studentId === this.searchForm.studentId) || {}
            if (this.searchForm.studentId) {
              this.fetchData()
            }
          }
        })
        .catch(() => {
          this.$message.error('获取学生列表失败')
        })
    },
    
    // 学生选择变更
    handleStudentChange(studentId) {
      this.currentStudent = this.studentOptions.find(item => item.studentId === studentId) || {}
      if (studentId) {
        this.fetchData()
      }
    },
    
    // 获取学生成绩统计数据
    fetchData() {
      if (!this.searchForm.studentId) {
        this.$message.warning('请选择学生')
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
          this.$message.error('获取成绩统计数据失败')
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    // 重置搜索条件
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
    
    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        // 初始化柱状图
        this.initBarChart()
        
        // 初始化饼图
        this.initPieChart()
      })
    },
    
    // 初始化柱状图
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
            formatter: '{value} 分'
          }
        },
        series: [
          {
            name: '成绩',
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
              formatter: '{c} 分'
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
      
      const { courseScores } = this.statisticsData
      
      // 统计各等级成绩数量
      const levels = [
        { name: '优秀 (90-100分)', value: 0, color: '#67C23A' },
        { name: '良好 (80-89分)', value: 0, color: '#409EFF' },
        { name: '中等 (70-79分)', value: 0, color: '#E6A23C' },
        { name: '及格 (60-69分)', value: 0, color: '#F56C6C' },
        { name: '不及格 (0-59分)', value: 0, color: '#909399' }
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
            name: '成绩等级',
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
    
    // 窗口大小变化时重绘图表
    resizeCharts() {
      if (this.barChart) {
        this.barChart.resize()
      }
      if (this.pieChart) {
        this.pieChart.resize()
      }
    },
    
    // 根据成绩返回不同的Tag类型
    getScoreTagType(score) {
      if (score >= 90) return 'success'
      if (score >= 75) return 'primary'
      if (score >= 60) return ''
      return 'danger'
    },
    
    // 根据成绩返回等级
    getScoreLevel(score) {
      if (score >= 90) return '优秀'
      if (score >= 80) return '良好'
      if (score >= 70) return '中等'
      if (score >= 60) return '及格'
      return '不及格'
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