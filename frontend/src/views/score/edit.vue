<template>
  <div class="container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <span>{{ isEdit ? 'Edit Score' : 'Enter Score' }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">Back to List</el-button>
      </div>
      
      <el-form :model="scoreForm" :rules="rules" ref="scoreForm" label-width="100px" class="scoreForm" v-if="!loading">
        <!-- Student Information -->
        <el-card class="info-card">
          <div slot="header" class="clearfix">
            <span>Student Information</span>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="Student ID">{{ studentInfo.studentId }}</el-descriptions-item>
            <el-descriptions-item label="Name">{{ studentInfo.name }}</el-descriptions-item>
            <el-descriptions-item label="Gender">{{ studentInfo.gender }}</el-descriptions-item>
            <el-descriptions-item label="Class">{{ studentInfo.class }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
        
        <!-- Course Information -->
        <el-card class="info-card">
          <div slot="header" class="clearfix">
            <span>Course Information</span>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="Course ID">{{ courseInfo.courseId }}</el-descriptions-item>
            <el-descriptions-item label="Course Name">{{ courseInfo.courseName }}</el-descriptions-item>
            <el-descriptions-item label="Teacher">{{ courseInfo.teacherName }}</el-descriptions-item>
            <el-descriptions-item label="Credits">{{ courseInfo.credit }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
        
        <!-- Score Entry -->
        <el-card class="info-card">
          <div slot="header" class="clearfix">
            <span>Score Information</span>
          </div>
          
          <el-form-item label="Score" prop="score">
            <el-input-number 
              v-model="scoreForm.score" 
              :min="0" 
              :max="100" 
              :precision="1"
              :step="0.5"
              style="width: 180px"
              placeholder="Enter score"></el-input-number>
            <el-tag v-if="scoreForm.score !== null" :type="getScoreTagType(scoreForm.score)" style="margin-left: 10px">
              {{ getScoreLevel(scoreForm.score) }}
            </el-tag>
          </el-form-item>
          
          <el-form-item label="Comment" prop="comment">
            <el-input 
              type="textarea" 
              v-model="scoreForm.comment" 
              :rows="3"
              :maxlength="200"
              show-word-limit
              placeholder="Enter comment (optional)"></el-input>
          </el-form-item>
        </el-card>
        
        <el-form-item class="action-bar">
          <el-button type="primary" @click="submitForm('scoreForm')" :loading="submitting">Save</el-button>
          <el-button @click="resetForm">Reset</el-button>
          <el-button @click="goBack">Cancel</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'ScoreEdit',
  data() {
    return {
      loading: true,
      submitting: false,
      scoreId: '',
      studentId: '',
      courseId: '',
      studentInfo: {},
      courseInfo: {},
      originalScore: null,
      scoreForm: {
        score: null,
        comment: ''
      },
      rules: {
        score: [
          { required: true, message: 'Please enter a score', trigger: 'blur' },
          { type: 'number', min: 0, max: 100, message: 'Score must be between 0-100', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    isEdit() {
      return this.originalScore !== null
    }
  },
  created() {
    // Get route parameters
    this.scoreId = this.$route.params.id
    this.studentId = this.$route.query.studentId || ''
    this.courseId = this.$route.query.courseId || ''
    
    if (!this.studentId || !this.courseId) {
      this.$message.error('Missing student ID or course ID parameters')
      this.goBack()
      return
    }
    
    this.fetchData()
  },
  methods: {
    ...mapActions({
      getStudentDetail: 'student/getStudentDetail',
      getCourseDetail: 'course/getCourseDetail',
      getScores: 'score/getScores',
      updateScore: 'score/updateScore'
    }),
    
    fetchData() {
      this.loading = true
      
      // Get student information
      const studentPromise = this.getStudentDetail(this.studentId)
        .then(data => {
          this.studentInfo = data
        })
      
      // Get course information
      const coursePromise = this.getCourseDetail(this.courseId)
        .then(data => {
          this.courseInfo = data
        })
      
      // Get current score information
      const scorePromise = this.getScores({
        studentId: this.studentId,
        courseId: this.courseId
      }).then(data => {
        if (data.records && data.records.length > 0) {
          const scoreData = data.records[0]
          this.scoreId = scoreData.id
          this.originalScore = scoreData.score
          this.scoreForm.score = scoreData.score
          this.scoreForm.comment = scoreData.comment || ''
        }
      })
      
      // Wait for all requests to complete
      Promise.all([studentPromise, coursePromise, scorePromise])
        .catch(() => {
          this.$message.error('Failed to fetch data')
          this.goBack()
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.submitting = true
          
          // Check if scoreId exists
          if (!this.scoreId) {
            this.$message.error('Course selection ID does not exist, cannot update score')
            this.submitting = false
            return
          }
          
          this.updateScore({
            id: this.scoreId,
            data: this.scoreForm
          })
            .then(() => {
              this.$message.success(`Score ${this.isEdit ? 'updated' : 'entered'} successfully`)
              this.goBack()
            })
            .catch(() => {
              this.submitting = false
            })
        } else {
          this.$message.error('Please fill out the form correctly')
          return false
        }
      })
    },
    
    resetForm() {
      this.scoreForm.score = this.originalScore
      this.scoreForm.comment = ''
    },
    
    goBack() {
      // Return to score list page
      this.$router.push('/score/list')
    },
    
    // Return different tag type based on score
    getScoreTagType(score) {
      if (score >= 90) return 'success'
      if (score >= 75) return 'primary'
      if (score >= 60) return ''
      return 'danger'
    },
    
    // Return grade level based on score
    getScoreLevel(score) {
      if (score >= 90) return 'Excellent'
      if (score >= 80) return 'Good'
      if (score >= 70) return 'Average'
      if (score >= 60) return 'Pass'
      return 'Fail'
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20px;
}
.scoreForm {
  max-width: 800px;
  margin: 0 auto;
}
.info-card {
  margin-bottom: 20px;
}
.action-bar {
  margin-top: 30px;
  text-align: center;
}
</style> 