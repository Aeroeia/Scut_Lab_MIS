<template>
  <div class="container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <span>{{ isEdit ? '修改成绩' : '录入成绩' }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回列表</el-button>
      </div>
      
      <el-form :model="scoreForm" :rules="rules" ref="scoreForm" label-width="100px" class="scoreForm" v-if="!loading">
        <!-- 学生信息 -->
        <el-card class="info-card">
          <div slot="header" class="clearfix">
            <span>学生信息</span>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="学号">{{ studentInfo.studentId }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ studentInfo.name }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ studentInfo.gender }}</el-descriptions-item>
            <el-descriptions-item label="班级">{{ studentInfo.class }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
        
        <!-- 课程信息 -->
        <el-card class="info-card">
          <div slot="header" class="clearfix">
            <span>课程信息</span>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="课程编号">{{ courseInfo.courseId }}</el-descriptions-item>
            <el-descriptions-item label="课程名称">{{ courseInfo.courseName }}</el-descriptions-item>
            <el-descriptions-item label="授课教师">{{ courseInfo.teacherName }}</el-descriptions-item>
            <el-descriptions-item label="学分">{{ courseInfo.credit }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
        
        <!-- 成绩录入 -->
        <el-card class="info-card">
          <div slot="header" class="clearfix">
            <span>成绩信息</span>
          </div>
          
          <el-form-item label="成绩" prop="score">
            <el-input-number 
              v-model="scoreForm.score" 
              :min="0" 
              :max="100" 
              :precision="1"
              :step="0.5"
              style="width: 180px"
              placeholder="请输入成绩"></el-input-number>
            <el-tag v-if="scoreForm.score !== null" :type="getScoreTagType(scoreForm.score)" style="margin-left: 10px">
              {{ getScoreLevel(scoreForm.score) }}
            </el-tag>
          </el-form-item>
          
          <el-form-item label="评语" prop="comment">
            <el-input 
              type="textarea" 
              v-model="scoreForm.comment" 
              :rows="3"
              :maxlength="200"
              show-word-limit
              placeholder="请输入评语（选填）"></el-input>
          </el-form-item>
        </el-card>
        
        <el-form-item class="action-bar">
          <el-button type="primary" @click="submitForm('scoreForm')" :loading="submitting">保存</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="goBack">取消</el-button>
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
          { required: true, message: '请输入成绩', trigger: 'blur' },
          { type: 'number', min: 0, max: 100, message: '成绩必须在0-100之间', trigger: 'blur' }
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
    // 获取路由参数
    this.scoreId = this.$route.params.id
    this.studentId = this.$route.query.studentId || ''
    this.courseId = this.$route.query.courseId || ''
    
    if (!this.studentId || !this.courseId) {
      this.$message.error('缺少学生ID或课程ID参数')
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
      
      // 获取学生信息
      const studentPromise = this.getStudentDetail(this.studentId)
        .then(data => {
          this.studentInfo = data
        })
      
      // 获取课程信息
      const coursePromise = this.getCourseDetail(this.courseId)
        .then(data => {
          this.courseInfo = data
        })
      
      // 获取当前成绩信息
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
      
      // 等待所有请求完成
      Promise.all([studentPromise, coursePromise, scorePromise])
        .catch(() => {
          this.$message.error('获取数据失败')
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
          
          // 判断是否有scoreId
          if (!this.scoreId) {
            this.$message.error('选课记录ID不存在，无法更新成绩')
            this.submitting = false
            return
          }
          
          this.updateScore({
            id: this.scoreId,
            data: this.scoreForm
          })
            .then(() => {
              this.$message.success(`成功${this.isEdit ? '修改' : '录入'}成绩`)
              this.goBack()
            })
            .catch(() => {
              this.submitting = false
            })
        } else {
          this.$message.error('请正确填写表单内容')
          return false
        }
      })
    },
    
    resetForm() {
      this.scoreForm.score = this.originalScore
      this.scoreForm.comment = ''
    },
    
    goBack() {
      // 如果是从成绩列表页进入，返回成绩列表页
      this.$router.push('/score/list')
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