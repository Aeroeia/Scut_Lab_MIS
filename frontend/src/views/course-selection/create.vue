<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Add Course Selection</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">Back to List</el-button>
      </div>
      
      <el-form :model="selectionForm" :rules="rules" ref="selectionForm" label-width="100px" class="selectionForm">
        <el-form-item label="Student" prop="studentId">
          <el-select 
            v-model="selectionForm.studentId" 
            filterable 
            placeholder="Select student" 
            @change="handleStudentChange"
            style="width: 100%"
            @click="handleStudentClick">
            <el-option
              v-for="item in studentOptions"
              :key="item.studentId"
              :label="`${item.studentId} - ${item.name}`"
              :value="item.studentId">
              <span>{{ item.studentId }} - {{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.class }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="Course" prop="courseId">
          <el-select 
            v-model="selectionForm.courseId" 
            filterable 
            placeholder="Select course" 
            @change="handleCourseChange"
            style="width: 100%"
            @click="handleCourseClick">
            <el-option
              v-for="item in courseOptions"
              :key="item.courseId"
              :label="`${item.courseId} - ${item.courseName}`"
              :value="item.courseId">
              <span>{{ item.courseId }} - {{ item.courseName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">
                {{ item.teacherName }} | {{ item.credit }} credits
              </span>
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="Select Year" prop="selectionYear">
          <el-date-picker
            v-model="selectionForm.selectionYear"
            type="year"
            value-format="yyyy"
            placeholder="Select year">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="Notes" prop="remark">
          <el-input type="textarea" v-model="selectionForm.remark" :rows="3" placeholder="Optional"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm('selectionForm')" :loading="loading">Submit</el-button>
          <el-button @click="resetForm('selectionForm')">Reset</el-button>
          <el-button @click="goBack">Cancel</el-button>
        </el-form-item>
      </el-form>
      
      <!-- Preview Information -->
      <el-card class="preview-card" v-if="showPreview">
        <div slot="header" class="clearfix">
          <span>Course Selection Preview</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Student ID">{{ selectionForm.studentId }}</el-descriptions-item>
          <el-descriptions-item label="Student Name">{{ selectionForm.studentName }}</el-descriptions-item>
          <el-descriptions-item label="Course ID">{{ selectionForm.courseId }}</el-descriptions-item>
          <el-descriptions-item label="Course Name">{{ selectionForm.courseName }}</el-descriptions-item>
          <el-descriptions-item label="Teacher">{{ selectionForm.teacherName }}</el-descriptions-item>
          <el-descriptions-item label="Credits">{{ selectionForm.credit }}</el-descriptions-item>
          <el-descriptions-item label="Selection Year">{{ selectionForm.selectionYear }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-card>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'CourseSelectionCreate',
  data() {
    return {
      loading: false,
      studentOptions: [],
      courseOptions: [],
      selectionForm: {
        studentId: '',
        studentName: '',
        courseId: '',
        courseName: '',
        teacherId: '',
        teacherName: '',
        credit: '',
        selectionYear: new Date().getFullYear().toString(),
        remark: ''
      },
      rules: {
        studentId: [
          { required: true, message: 'Please select a student', trigger: 'change' }
        ],
        courseId: [
          { required: true, message: 'Please select a course', trigger: 'change' }
        ],
        selectionYear: [
          { required: true, message: 'Please select a year', trigger: 'change' }
        ]
      },
      dataLoaded: false
    }
  },
  computed: {
    showPreview() {
      return this.selectionForm.studentId && this.selectionForm.courseId
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      // 在组件实例创建后可进行操作，但不触发自动请求
    })
  },
  mounted() {
    // 挂载时只准备界面，不发送请求
  },
  methods: {
    ...mapActions('courseSelection', ['addCourseSelection']),
    ...mapActions('student', ['getStudents']),
    ...mapActions('course', ['getCourses']),
    
    // 只在需要时获取学生列表
    fetchStudents() {
      if (this.studentOptions.length > 0) return; // 如果已经有数据，不重复请求
      
      this.getStudents({ size: 1000 })
        .then(data => {
          this.studentOptions = data.records || []
        })
        .catch(() => {
          this.$message.error('Failed to get student list')
        })
    },
    
    // 只在需要时获取课程列表
    fetchCourses() {
      if (this.courseOptions.length > 0) return; // 如果已经有数据，不重复请求
      
      this.getCourses({ size: 1000 })
        .then(data => {
          this.courseOptions = data.records || []
        })
        .catch(() => {
          this.$message.error('Failed to get course list')
        })
    },
    
    // 调整为点击选择框时加载数据
    handleStudentClick() {
      this.fetchStudents()
    },
    
    handleCourseClick() {
      this.fetchCourses()
    },
    
    // Student selection change
    handleStudentChange(studentId) {
      const student = this.studentOptions.find(item => item.studentId === studentId)
      if (student) {
        this.selectionForm.studentName = student.name
      }
    },
    
    // Course selection change
    handleCourseChange(courseId) {
      const course = this.courseOptions.find(item => item.courseId === courseId)
      if (course) {
        this.selectionForm.courseName = course.courseName
        this.selectionForm.teacherId = course.teacherId
        this.selectionForm.teacherName = course.teacherName
        this.selectionForm.credit = course.credit
      }
    },
    
    // Submit form
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true
          
          this.addCourseSelection(this.selectionForm)
            .then(() => {
              this.$message.success('Course selection added successfully')
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
    
    // Reset form
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.selectionForm = {
        studentId: '',
        studentName: '',
        courseId: '',
        courseName: '',
        teacherId: '',
        teacherName: '',
        credit: '',
        selectionYear: new Date().getFullYear().toString(),
        remark: ''
      }
    },
    
    // Return to list
    goBack() {
      this.$router.push('/course-selection/list')
    }
  }
}
</script>

<style scoped>
.selectionForm {
  width: 600px;
  margin: 0 auto;
}
.preview-card {
  margin-top: 20px;
  width: 600px;
  margin-left: auto;
  margin-right: auto;
}
</style> 