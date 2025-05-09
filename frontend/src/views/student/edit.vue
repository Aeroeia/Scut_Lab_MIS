<template>
  <div class="container">
    <el-card v-loading="pageLoading">
      <div slot="header">
        <span>编辑学生信息</span>
      </div>
      
      <el-form :model="studentForm" :rules="rules" ref="studentForm" label-width="100px" class="studentForm" v-if="!pageLoading">
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="studentForm.studentId" placeholder="请输入学号" disabled></el-input>
        </el-form-item>
        
        <el-form-item label="姓名" prop="name">
          <el-input v-model="studentForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="studentForm.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="入学年龄" prop="ageAtEnrollment">
          <el-input-number v-model="studentForm.ageAtEnrollment" :min="10" :max="50" placeholder="请输入入学年龄"></el-input-number>
        </el-form-item>
        
        <el-form-item label="入学年份" prop="enrollmentYear">
          <el-date-picker
            v-model="enrollmentDate"
            type="year"
            placeholder="选择年份"
            value-format="yyyy"
            @change="handleYearChange">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="班级" prop="class">
          <el-input v-model="studentForm.class" placeholder="请输入班级"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm('studentForm')" :loading="submitLoading">保存</el-button>
          <el-button @click="resetForm('studentForm')">重置</el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'StudentEdit',
  data() {
    return {
      pageLoading: false,
      submitLoading: false,
      studentId: '',
      enrollmentDate: '',
      studentForm: {
        studentId: '',
        name: '',
        gender: '男',
        ageAtEnrollment: 18,
        enrollmentYear: new Date().getFullYear(),
        class: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        ageAtEnrollment: [
          { required: true, message: '请输入入学年龄', trigger: 'blur' },
          { type: 'number', min: 10, max: 50, message: '年龄必须在10到50岁之间', trigger: 'blur' }
        ],
        enrollmentYear: [
          { required: true, message: '请选择入学年份', trigger: 'change' }
        ],
        class: [
          { required: true, message: '请输入班级', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.studentId = this.$route.params.id
    this.getStudentInfo()
  },
  methods: {
    ...mapActions('student', ['getStudentDetail', 'updateStudent']),
    
    getStudentInfo() {
      if (!this.studentId) {
        this.$message.error('学生ID不能为空')
        this.goBack()
        return
      }
      
      this.pageLoading = true
      this.getStudentDetail(this.studentId)
        .then(data => {
          this.studentForm = { ...data }
          this.enrollmentDate = String(data.enrollmentYear)
        })
        .catch(() => {
          this.$message.error('获取学生信息失败')
          this.goBack()
        })
        .finally(() => {
          this.pageLoading = false
        })
    },
    
    handleYearChange(value) {
      this.studentForm.enrollmentYear = parseInt(value)
    },
    
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.submitLoading = true
          
          this.updateStudent({
            studentId: this.studentId,
            data: this.studentForm
          })
            .then(() => {
              this.$message.success('更新成功')
              this.goBack()
            })
            .catch(() => {
              this.submitLoading = false
            })
        } else {
          this.$message.error('请正确填写表单')
          return false
        }
      })
    },
    
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.getStudentInfo()
    },
    
    goBack() {
      this.$router.push('/student/list')
    }
  }
}
</script>

<style scoped>
.studentForm {
  width: 600px;
  margin: 0 auto;
}
</style> 