<template>
  <div class="container">
    <el-card v-loading="pageLoading">
      <div slot="header" class="clearfix">
        <span>Edit Course</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">Back to List</el-button>
      </div>
      
      <el-form :model="courseForm" :rules="rules" ref="courseForm" label-width="150px" class="courseForm" v-if="!pageLoading">
        <el-form-item label="Course ID" prop="courseId">
          <el-input v-model="courseForm.courseId" placeholder="Enter course ID" disabled></el-input>
        </el-form-item>
        
        <el-form-item label="Course Name" prop="courseName" >
          <el-input v-model="courseForm.courseName" placeholder="Enter course name"></el-input>
        </el-form-item>
        
        <el-form-item label="Teacher" prop="teacherId">
          <el-select v-model="courseForm.teacherId" placeholder="Select teacher" filterable @change="handleTeacherChange">
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
            <el-option label=" First Semester" value="1"></el-option>
            <el-option label=" Second Semester" value="2"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="Description" prop="description">
          <el-input type="textarea" v-model="courseForm.description" placeholder="Enter course description" :rows="4"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm('courseForm')" :loading="submitLoading">Save</el-button>
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
  name: 'CourseEdit',
  data() {
    return {
      pageLoading: true,
      submitLoading: false,
      courseId: '',
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
        courseName: [
          { required: true, message: 'Please enter course name', trigger: 'blur' },
          { min: 2, max: 50, message: 'Length should be 2 to 50 characters', trigger: 'blur' }
        ],
        teacherId: [
          { required: true, message: 'Please select a teacher', trigger: 'change' }
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
    this.courseId = this.$route.params.id
    this.fetchTeachers().then(() => {
      this.getCourseInfo()
    })
  },
  methods: {
    ...mapActions('course', ['getCourseDetail', 'updateCourse']),
    ...mapActions('teacher', ['getTeachers']),
    
    // Get teacher list
    fetchTeachers() {
      return this.getTeachers({ size: 1000 })
        .then(data => {
          this.teacherOptions = data.records || []
        })
        .catch(() => {
          this.$message.error('Failed to get teacher list')
        })
    },
    
    // Get course information
    getCourseInfo() {
      if (!this.courseId) {
        this.$message.error('Course ID cannot be empty')
        this.goBack()
        return
      }
      
      this.pageLoading = true
      this.getCourseDetail(this.courseId)
        .then(data => {
          this.courseForm = { ...data }
        })
        .catch(() => {
          this.$message.error('Failed to get course information')
          this.goBack()
        })
        .finally(() => {
          this.pageLoading = false
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
          this.submitLoading = true
          
          this.updateCourse({
            courseId: this.courseId,
            data: this.courseForm
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
      this.getCourseInfo()
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