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
            <el-descriptions-item label="Hours" v-if="courseInfo.hours">{{ courseInfo.hours }}</el-descriptions-item>
            <el-descriptions-item label="Semester" v-if="courseInfo.semester">{{ courseInfo.semester }}</el-descriptions-item>
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
      teacherId: '',
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
    this.scoreId = this.$route.query.id || ''
    this.studentId = this.$route.query.studentId || ''
    this.courseId = this.$route.query.courseId || ''
    this.teacherId = this.$route.query.teacherId || ''
    
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
      
      // 创建Promise数组
      const promises = []
      
      // 获取学生信息 - 修复传参格式
      if (this.studentId) {
        const studentPromise = this.getStudentDetail({
          studentId: this.studentId,
          params: {}
        }).then(data => {
          this.studentInfo = data || {}
        }).catch(error => {
          console.error('获取学生信息失败:', error)
          this.$message.error('获取学生信息失败')
        })
        
        promises.push(studentPromise)
      }
      
      // 获取课程信息 - 必须传递teacherId参数
      if (this.courseId) {
        const coursePromise = this.getCourseDetail({
          courseId: this.courseId,
          teacherId: this.teacherId
        }).then(data => {
          this.courseInfo = data || {}
        }).catch(error => {
          console.error('获取课程信息失败:', error)
          this.$message.error('获取课程信息失败')
        })
        
        promises.push(coursePromise)
      }
      
      // 获取当前成绩信息
      if (this.studentId && this.courseId) {
        const scorePromise = this.getScores({
          studentId: this.studentId,
          courseId: this.courseId
        }).then(data => {
          console.log('后端返回的原始数据:', data)
          
          // Vuex store中的getScores action已经处理了response.data，所以这里直接使用
          // 判断数据格式并处理 - 可能是 {score: 85.5, scoreId: 151} 或者 records 数组
          
          if (data && data.score !== undefined && data.scoreId !== undefined) {
            // 直接返回的成绩对象格式
            this.scoreId = data.scoreId || this.scoreId
            this.originalScore = data.score
            this.scoreForm.score = data.score
            this.scoreForm.comment = data.comment || ''
          } else if (data && data.records && data.records.length > 0) {
            // records数组格式
            const scoreData = data.records[0]
            this.scoreId = scoreData.id || this.scoreId
            this.originalScore = scoreData.score
            this.scoreForm.score = scoreData.score 
            this.scoreForm.comment = scoreData.comment || ''
          }
          
          console.log('处理后的成绩数据:', {
            scoreId: this.scoreId,
            originalScore: this.originalScore,
            currentScore: this.scoreForm.score,
            comment: this.scoreForm.comment
          })
        }).catch(error => {
          console.error('获取成绩信息失败:', error)
          this.$message.error('获取成绩信息失败')
        })
        
        promises.push(scorePromise)
      }
      
      // 等待所有请求完成
      Promise.all(promises)
        .finally(() => {
          this.loading = false
        })
    },
    
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.submitting = true
          
          // 检查scoreId是否存在，如果从query参数获取，则使用它
          if (this.$route.query.id && !this.scoreId) {
            this.scoreId = this.$route.query.id
          }
          
          // 再次检查scoreId
          if (!this.scoreId) {
            this.$message.error('找不到选课记录ID，无法更新成绩')
            this.submitting = false
            return
          }
          
          const scoreData = {
            score: this.scoreForm.score,
          }
          
          console.log('准备提交的数据:', {
            id: this.scoreId,
            scoreData: scoreData
          })
          
          this.updateScore({
            id: this.scoreId,
            data: scoreData
          })
            .then(() => {
              this.$message.success(`成绩${this.isEdit ? '更新' : '录入'}成功`)
              this.goBack()
            })
            .catch(error => {
              console.error('更新成绩失败：', error)
              this.$message.error(`成绩${this.isEdit ? '更新' : '录入'}失败：${error.message || '未知错误'}`)
              this.submitting = false
            })
        } else {
          this.$message.error('请正确填写表单')
          return false
        }
      })
    },
    
    resetForm() {
      this.scoreForm.score = this.originalScore
      this.scoreForm.comment = ''
    },
    
    goBack() {
      // 返回到选课列表页面而不是成绩列表
      this.$router.push('/course-selection/list')
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