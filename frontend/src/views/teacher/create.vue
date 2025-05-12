<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Add Teacher</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">Back to List</el-button>
      </div>
      
      <el-form :model="teacherForm" :rules="rules" ref="teacherForm" label-width="100px">
        <el-form-item label="ID" prop="teacherId">
          <el-input v-model="teacherForm.teacherId" placeholder="Enter ID"></el-input>
        </el-form-item>
        
        <el-form-item label="Name" prop="name">
          <el-input v-model="teacherForm.name" placeholder="Enter name"></el-input>
        </el-form-item>
        
        <el-form-item label="Gender" prop="gender">
          <el-radio-group v-model="teacherForm.gender">
            <el-radio label="男">Male</el-radio>
            <el-radio label="女">Female</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="Title" prop="title">
          <el-select v-model="teacherForm.title" placeholder="Select title">
            <el-option label="Lecturer" value="讲师"></el-option>
            <el-option label="Associate Professor" value="副教授"></el-option>
            <el-option label="Professor" value="教授"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="Department" prop="department">
          <el-input v-model="teacherForm.department" placeholder="Enter department"></el-input>
        </el-form-item>
        
        <el-form-item label="Email" prop="email">
          <el-input v-model="teacherForm.email" placeholder="Enter email"></el-input>
        </el-form-item>
        
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="teacherForm.phone" placeholder="Enter phone number"></el-input>
        </el-form-item>
        
        <el-form-item label="Hire Date" prop="hireDate">
          <el-date-picker
            v-model="teacherForm.hireDate"
            type="date"
            placeholder="Select hire date"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="Password" prop="password">
          <el-input v-model="teacherForm.password" type="password" placeholder="Enter password"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="loading">Submit</el-button>
          <el-button @click="resetForm">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'TeacherCreate',
  data() {
    // Validate teacher ID
    const validateTeacherId = (rule, value, callback) => {
      if (!value) {
        callback(new Error('Please enter teacher ID'))
      } else if (!/^\d{8}$/.test(value)) {
        callback(new Error('ID must be 8 digits'))
      } else {
        callback()
      }
    }
    
    // Validate email
    const validateEmail = (rule, value, callback) => {
      if (!value) {
        callback()
      } else if (!/^[\w.-]+@[\w.-]+\.\w+$/.test(value)) {
        callback(new Error('Please enter a valid email format'))
      } else {
        callback()
      }
    }
    
    // Validate phone
    const validatePhone = (rule, value, callback) => {
      if (!value) {
        callback()
      } else if (!/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error('Please enter a valid phone number format'))
      } else {
        callback()
      }
    }
    
    return {
      loading: false,
      teacherForm: {
        teacherId: '',
        name: '',
        gender: '男',
        title: '',
        department: '',
        email: '',
        phone: '',
        hireDate: '',
        password: '123456' // Default password
      },
      rules: {
        teacherId: [
          { required: true, message: 'Please enter teacher ID', trigger: 'blur' },
          { validator: validateTeacherId, trigger: 'blur' }
        ],
        name: [
          { required: true, message: 'Please enter name', trigger: 'blur' },
          { min: 2, max: 20, message: 'Length should be 2 to 20 characters', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: 'Please select gender', trigger: 'change' }
        ],
        title: [
          { required: true, message: 'Please select title', trigger: 'change' }
        ],
        department: [
          { required: true, message: 'Please enter department', trigger: 'blur' }
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ],
        phone: [
          { validator: validatePhone, trigger: 'blur' }
        ],
        hireDate: [
          { required: true, message: 'Please select hire date', trigger: 'change' }
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 6, message: 'Password length cannot be less than 6 characters', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    ...mapActions('teacher', ['createTeacher']),
    
    submitForm() {
      this.$refs.teacherForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.createTeacher(this.teacherForm)
            .then(() => {
              this.$message.success('Teacher added successfully')
              this.goBack()
            })
            .finally(() => {
              this.loading = false
            })
        } else {
          this.$message.error('Please fill in the form correctly')
          return false
        }
      })
    },
    
    resetForm() {
      this.$refs.teacherForm.resetFields()
    },
    
    goBack() {
      this.$router.push('/teacher/list')
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20px;
}
</style> 