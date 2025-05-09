<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>班级成绩统计</span>
      </div>
      
      <!-- 查询条件 -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="班级">
          <el-select 
            v-model="searchForm.className" 
            filterable 
            placeholder="请选择班级"
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
      
      <!-- 班级信息展示 -->
      <el-card v-if="showClassInfo" class="mb-20">
        <div slot="header" class="clearfix">
          <span>班级统计信息</span>
        </div>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="班级名称">{{ searchForm.className }}</el-descriptions-item>
          <el-descriptions-item label="学生人数">{{ statisticsData.studentCount || 0 }} 人</el-descriptions-item>
          <el-descriptions-item label="平均成绩">
            <span class="average-score" :class="getScoreClass(statisticsData.averageScore)">
              {{ statisticsData.averageScore ? statisticsData.averageScore.toFixed(1) : '-' }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="最高分">
            <span class="score-excellent">{{ statisticsData.highestScore || '-' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="最低分">
            <span class="score-fail">{{ statisticsData.lowestScore || '-' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="优良率">
            {{ statisticsData.excellentRate ? (statisticsData.excellentRate * 100).toFixed(1) + '%' : '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
      
      <!-- 成绩统计图表 -->
      <div v-loading="loading">
        <el-row :gutter="20" v-if="showCharts">
          <!-- 成绩分布柱状图 -->
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="clearfix">
                <span>成绩分布</span>
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
        
        <!-- 学生成绩表格 -->
        <el-card class="mt-20" v-if="showTable">
          <div slot="header" class="clearfix">
            <span>班级学生成绩列表</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="handleExport"
              :disabled="!showTable">
              导出Excel
            </el-button>
          </div>
          <el-table
            :data="statisticsData.studentScores"
            border
            style="width: 100%">
            <el-table-column prop="studentId" label="学号" width="120" align="center"></el-table-column>
            <el-table-column prop="name" label="姓名" width="120" align="center"></el-table-column>
            <el-table-column prop="gender" label="性别" width="80" align="center"></el-table-column>
            <el-table-column prop="averageScore" label="平均成绩" width="100" align="center">
              <template slot-scope="scope">
                <span :class="getScoreClass(scope.row.averageScore)">
                  {{ scope.row.averageScore ? scope.row.averageScore.toFixed(1) : '-' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="rank" label="班级排名" width="100" align="center"></el-table-column>
            <el-table-column label="操作" width="120" align="center">
              <template slot-scope="scope">
                <el-button 
                  type="text" 
                  size="small" 
                  @click="viewStudentDetail(scope.row)">
                  详细成绩
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        
        <el-empty v-if="!loading && !showCharts" description="请选择班级查看成绩统计"></el-empty>
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
    
    // 获取班级列表
    fetchClassList() {
      this.getStudents({ size: 1000 })
        .then(data => {
          // 从学生数据中提取不重复的班级
          const classList = [...new Set(data.records.map(item => item.class))].filter(Boolean)
          this.classOptions = classList.sort()
          
          // 如果路由传入了班级参数，自动设置
          if (this.$route.query.className) {
            this.searchForm.className = this.$route.query.className
            this.fetchData()
          }
        })
        .catch(() => {
          this.$message.error('获取班级列表失败')
        })
    },
    
    // 班级选择变更
    handleClassChange(className) {
      if (className) {
        this.fetchData()
      }
    },
    
    // 获取班级成绩统计数据
    fetchData() {
      if (!this.searchForm.className) {
        this.$message.warning('请选择班级')
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
          this.$message.error('获取班级成绩统计数据失败')
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    // 重置搜索条件
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
      
      // 计算成绩区间分布
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
          formatter: '{a} <br/>{b}: {c}人 ({d}%)'
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
      this.$message.success('成绩表导出成功')
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