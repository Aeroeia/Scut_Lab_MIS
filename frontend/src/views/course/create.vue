<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Add Course</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">Back to List</el-button>
      </div>
      
      <el-form :model="courseForm" :rules="rules" ref="courseForm" label-width="150px" class="courseForm">
        <el-form-item label="Course ID" prop="courseId">
          <el-input v-model="courseForm.courseId" placeholder="Enter course ID"></el-input>
        </el-form-item>
        
        <el-form-item label="Course Name" prop="courseName">
          <el-input v-model="courseForm.courseName" placeholder="Enter course name"></el-input>
        </el-form-item>
        
        <el-form-item label="Instructor" prop="teacherId">
          <el-select v-model="courseForm.teacherId" placeholder="Select instructor" filterable @change="handleTeacherChange">
            <el-option
              v-for="item in teacherOptions"
              :key="item.teacherId"
              :label="item.name"
              :value="item.teacherId">
              <span>{{ item.teacherId }} - {{ item.name }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="Credits" prop="credit">
          <el-input-number v-model="courseForm.credit" :min="1" :max="10" placeholder="Enter credits"></el-input-number>
        </el-form-item>
        
        <el-form-item label="Hours" prop="hours">
          <el-input-number v-model="courseForm.hours" :min="8" :max="120" placeholder="Enter hours"></el-input-number>
        </el-form-item>
        
        <el-form-item label="Semester" prop="semester">
          <el-select v-model="courseForm.semester" placeholder="Select semester">
            <el-option label="First Semester" value="1"></el-option>
            <el-option label="Second Semester" value="2"></el-option>

          </el-select>
        </el-form-item>
        
        <el-form-item label="Description" prop="description">
          <el-input type="textarea" v-model="courseForm.description" placeholder="Enter course description" :rows="4"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm('courseForm')" :loading="loading">Save</el-button>
          <el-button @click="resetForm('courseForm')">Reset</el-button>
          <el-button @click="goBack">Back</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'CourseCreate',
  data() {
    // Validate if course ID is an 8-digit number
    const validateCourseId = (rule, value, callback) => {
      if (value.length !== 9) {
        callback(new Error('Course ID must be 9 digits'))
      }
      else {
        callback()
      }
    }
    
    return {
      loading: false,
      teacherOptions: [],
      courseForm: {
        courseId: '',
        courseName: '',
        teacherId: '',
        teacherName: '',
        credit: 3,
        hours: 48,
        semester: '',
        description: ''
      },
      rules: {
        courseId: [
          { required: true, message: 'Please enter course ID', trigger: 'blur' },
          { validator: validateCourseId, trigger: 'blur' }
        ],
        courseName: [
          { required: true, message: 'Please enter course name', trigger: 'blur' },
          { min: 2, max: 50, message: 'Length should be 2 to 50 characters', trigger: 'blur' }
        ],
        teacherId: [
          { required: true, message: 'Please select an instructor', trigger: 'change' }
        ],
        credit: [
          { required: true, message: 'Please enter credits', trigger: 'blur' },
          { type: 'number', min: 1, max: 10, message: 'Credits should be between 1-10', trigger: 'blur' }
        ],
        hours: [
          { required: true, message: 'Please enter hours', trigger: 'blur' },
          { type: 'number', min: 8, max: 120, message: 'Hours should be between 8-120', trigger: 'blur' }
        ],
        semester: [
          { required: true, message: 'Please select a semester', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.fetchTeachers()
  },
  methods: {
    ...mapActions('course', ['addCourse']),
    ...mapActions('teacher', ['getTeachers']),
    
    // Get teacher list
    fetchTeachers() {
      this.getTeachers({ size: 1000 })
        .then(data => {
          this.teacherOptions = data.records || []
        })
        .catch(() => {
          this.$message.error('Failed to get teacher list')
        })
    },
    
    // Set teacher name based on teacher ID
    handleTeacherChange(teacherId) {
      const teacher = this.teacherOptions.find(item => item.teacherId === teacherId)
      if (teacher) {
        this.courseForm.teacherName = teacher.name
      }
    },
    
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true
          
          this.addCourse(this.courseForm)
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
    },
    
    goBack() {
      this.$router.push('/course/list')
    }
  }
}
</script>

<style scoped>
.courseForm {
  width: 600px;
  margin: 0 auto;
}
</style> 