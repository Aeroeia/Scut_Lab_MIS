<template>
  <div class="container">
    <div v-loading="pageLoading">
      <!-- 基本信息卡片 -->
      <el-card class="mb-20">
        <div slot="header" class="clearfix">
          <span>学生基本信息</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="goEdit">编辑</el-button>
        </div>
        
        <el-descriptions :column="2" border v-if="studentInfo">
          <el-descriptions-item label="学号">{{ studentInfo.studentId }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ studentInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ studentInfo.gender }}</el-descriptions-item>
          <el-descriptions-item label="班级">{{ studentInfo.class }}</el-descriptions-item>
          <el-descriptions-item label="入学年龄">{{ studentInfo.ageAtEnrollment }}</el-descriptions-item>
          <el-descriptions-item label="入学年份">{{ studentInfo.enrollmentYear }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
      
      <!-- 选课信息卡片 -->
      <el-card>
        <div slot="header" class="clearfix">
          <span>选课信息</span>
          <div style="float: right">
            <el-select v-model="selectedYear" placeholder="选择年份" size="small" @change="fetchCourses" style="width: 120px; margin-right: 10px;">
              <el-option
                v-for="year in yearOptions"
                :key="year"
                :label="year + '年'"
                :value="year">
              </el-option>
            </el-select>
          </div>
        </div>
        
        <el-table :data="courseList" border style="width: 100%">
          <el-table-column prop="courseId" label="课程编号" width="120" align="center"></el-table-column>
          <el-table-column prop="courseName" label="课程名称" min-width="180" align="center"></el-table-column>
          <el-table-column prop="teacherId" label="教师编号" width="120" align="center"></el-table-column>
          <el-table-column prop="teacherName" label="教师姓名" width="120" align="center"></el-table-column>
          <el-table-column prop="credit" label="学分" width="80" align="center"></el-table-column>
          <el-table-column prop="selectionYear" label="选课年份" width="100" align="center"></el-table-column>
          <el-table-column prop="score" label="成绩" width="100" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.score !== null ? scope.row.score : '未录入' }}</span>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="no-data" v-if="courseList.length === 0">
          <el-empty description="暂无选课信息"></el-empty>
        </div>
      </el-card>
      
      <div class="action-bar mt-20">
        <el-button @click="goBack">返回</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'StudentDetail',
  data() {
    return {
      pageLoading: false,
      studentId: '',
      studentInfo: null,
      courseList: [],
      selectedYear: new Date().getFullYear(),
      yearOptions: []
    }
  },
  created() {
    this.studentId = this.$route.params.id
    this.initYearOptions()
    this.loadData()
  },
  methods: {
    ...mapActions('student', ['getStudentDetail', 'getStudentCourses']),
    
    initYearOptions() {
      const currentYear = new Date().getFullYear()
      this.yearOptions = []
      for (let i = 0; i < 5; i++) {
        this.yearOptions.push(currentYear - i)
      }
    },
    
    loadData() {
      if (!this.studentId) {
        this.$message.error('学生ID不能为空')
        this.goBack()
        return
      }
      
      this.pageLoading = true
      this.getStudentDetail(this.studentId)
        .then(data => {
          this.studentInfo = data
          this.fetchCourses()
        })
        .catch(() => {
          this.$message.error('获取学生信息失败')
          this.goBack()
        })
        .finally(() => {
          this.pageLoading = false
        })
    },
    
    fetchCourses() {
      this.getStudentCourses({
        studentId: this.studentId,
        params: { year: this.selectedYear }
      })
        .then(data => {
          this.courseList = data.courses || []
        })
        .catch(() => {
          this.$message.error('获取选课信息失败')
        })
    },
    
    goEdit() {
      this.$router.push(`/student/edit/${this.studentId}`)
    },
    
    goBack() {
      this.$router.push('/student/list')
    }
  }
}
</script>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.action-bar {
  text-align: center;
}

.no-data {
  margin: 20px 0;
}
</style> 