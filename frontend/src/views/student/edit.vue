<template>
  <div class="container">
    <el-card v-loading="pageLoading">
      <div slot="header">
        <span>Edit Student Information</span>
      </div>
      
      <el-form :model="studentForm" :rules="rules" ref="studentForm" label-width="100px" class="studentForm" v-if="!pageLoading">
        <el-form-item label="Student ID" prop="studentId">
          <el-input v-model="studentForm.studentId" placeholder="Enter student ID" disabled></el-input>
        </el-form-item>
        
        <el-form-item label="Name" prop="name">
          <el-input v-model="studentForm.name" placeholder="Enter name"></el-input>
        </el-form-item>
        
        <el-form-item label="Gender" prop="gender">
          <el-radio-group v-model="studentForm.gender">
            <el-radio label="男">Male</el-radio>
            <el-radio label="女">Female</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="Age at Enrollment" prop="ageAtEnrollment">
          <el-input-number v-model="studentForm.ageAtEnrollment" :min="10" :max="50" placeholder="Enter age at enrollment"></el-input-number>
        </el-form-item>
        
        <el-form-item label="Enrollment Year" prop="enrollmentYear">
          <el-date-picker
            v-model="enrollmentDate"
            type="year"
            placeholder="Select year"
            value-format="yyyy"
            @change="handleYearChange">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="Class" prop="class">
          <el-input v-model="studentForm.class" placeholder="Enter class"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm('studentForm')" :loading="submitLoading">Save</el-button>
          <el-button @click="resetForm('studentForm')">Reset</el-button>
          <el-button @click="goBack">Back</el-button>
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
          { required: true, message: 'Please enter a name', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: 'Please select gender', trigger: 'change' }
        ],
        ageAtEnrollment: [
          { required: true, message: 'Please enter age at enrollment', trigger: 'blur' },
          { type: 'number', min: 10, max: 50, message: 'Age must be between 10 and 50', trigger: 'blur' }
        ],
        enrollmentYear: [
          { required: true, message: 'Please select enrollment year', trigger: 'change' }
        ],
        class: [
          { required: true, message: 'Please enter class', trigger: 'blur' }
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
        this.$message.error('Student ID cannot be empty')
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
          this.$message.error('Failed to get student information')
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
              this.$message.success('Updated successfully')
              this.goBack()
            })
            .catch(() => {
              this.submitLoading = false
            })
        } else {
          this.$message.error('Please fill out the form correctly')
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