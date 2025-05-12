<template>
  <div class="container">
    <div v-loading="pageLoading">
      <!-- Basic Information Card -->
      <el-card class="mb-20">
        <div slot="header" class="clearfix">
          <span>Student Basic Information</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="goEdit">Edit</el-button>
        </div>
        
        <el-descriptions :column="2" border v-if="studentInfo">
          <el-descriptions-item label="Student ID">{{ studentInfo.studentId }}</el-descriptions-item>
          <el-descriptions-item label="Name">{{ studentInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="Gender">{{ studentInfo.gender }}</el-descriptions-item>
          <el-descriptions-item label="Class">{{ studentInfo.class }}</el-descriptions-item>
          <el-descriptions-item label="Age at Enrollment">{{ studentInfo.ageAtEnrollment }}</el-descriptions-item>
          <el-descriptions-item label="Enrollment Year">{{ studentInfo.enrollmentYear }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
      
      <!-- Course Information Card -->
      <el-card>
        <div slot="header" class="clearfix">
          <span>Course Registration Information</span>
          <div style="float: right">
            <el-select v-model="selectedYear" placeholder="Select year" size="small" @change="fetchCourses" style="width: 120px; margin-right: 10px;">
              <el-option
                v-for="year in yearOptions"
                :key="year"
                :label="year + ' year'"
                :value="year">
              </el-option>
            </el-select>
          </div>
        </div>
        
        <el-table :data="courseList" border style="width: 100%">
          <el-table-column prop="courseId" label="Course ID" width="120" align="center"></el-table-column>
          <el-table-column prop="courseName" label="Course Name" min-width="180" align="center"></el-table-column>
          <el-table-column prop="teacherId" label="Teacher ID" width="120" align="center"></el-table-column>
          <el-table-column prop="teacherName" label="Teacher Name" width="120" align="center"></el-table-column>
          <el-table-column prop="credit" label="Credits" width="80" align="center"></el-table-column>
          <el-table-column prop="selectionYear" label="Registration Year" width="100" align="center"></el-table-column>
          <el-table-column prop="score" label="Score" width="100" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.score !== null ? scope.row.score : 'Not entered' }}</span>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="no-data" v-if="courseList.length === 0">
          <el-empty description="No course registration information"></el-empty>
        </div>
      </el-card>
      
      <div class="action-bar mt-20">
        <el-button @click="goBack">Back</el-button>
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
        this.$message.error('Student ID cannot be empty')
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
          this.$message.error('Failed to get student information')
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
          this.$message.error('Failed to get course registration information')
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