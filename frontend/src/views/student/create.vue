<template>
  <div class="container">
    <el-card>
      <div slot="header">
        <span>Add Student</span>
      </div>
      
      <el-form :model="studentForm" :rules="rules" ref="studentForm" label-width="150px" class="studentForm">
        <el-form-item label="Student ID" prop="studentId">
          <el-input v-model="studentForm.studentId" placeholder="Enter student ID"></el-input>
        </el-form-item>
        
        <el-form-item label="Name" prop="name">
          <el-input v-model="studentForm.name" placeholder="Enter name"></el-input>
        </el-form-item>
        
        <el-form-item label="Gender" prop="gender">
          <el-radio-group v-model="studentForm.gender">
            <el-radio label="Male">Male</el-radio>
            <el-radio label="Female">Female</el-radio>
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
          <el-button type="primary" @click="submitForm('studentForm')" :loading="loading">Save</el-button>
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
  name: 'StudentCreate',
  data() {
    // Validate if student ID is a 10-digit number
    const validateStudentId = (rule, value, callback) => {
      if (value.length !== 10) {
        callback(new Error('Student ID must be 10 digits'))
      } else {
        callback()
      }
    }
    
    return {
      loading: false,
      enrollmentDate: '',
      studentForm: {
        studentId: '',
        name: '',
        gender: '',
        ageAtEnrollment: 18,
        enrollmentYear: new Date().getFullYear(),
        class: ''
      },
      rules: {
        studentId: [
          { required: true, message: 'Please enter student ID', trigger: 'blur' },
          { validator: validateStudentId, trigger: 'blur' }
        ],
        name: [
          { required: true, message: 'Please enter name', trigger: 'blur' }
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
  beforeRouteEnter(to, from, next) {
    next(vm => {
      // 在组件实例创建后可以进行操作，但不触发请求
    })
  },
  methods: {
    ...mapActions('student', ['addStudent']),
    
    handleYearChange(value) {
      this.studentForm.enrollmentYear = parseInt(value)
    },
    
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true
          
          this.addStudent(this.studentForm)
            .then(() => {
              this.$message.success('Added successfully')
              this.goBack()
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          this.$message.error('Please fill out the form correctly')
          return false
        }
      })
    },
    
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.enrollmentDate = ''
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