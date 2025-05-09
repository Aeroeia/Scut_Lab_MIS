<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>添加选课</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回列表</el-button>
      </div>
      
      <el-form :model="selectionForm" :rules="rules" ref="selectionForm" label-width="100px" class="selectionForm">
        <el-form-item label="学生" prop="studentId">
          <el-select 
            v-model="selectionForm.studentId" 
            filterable 
            placeholder="请选择学生" 
            @change="handleStudentChange"
            style="width: 100%">
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
        
        <el-form-item label="课程" prop="courseId">
          <el-select 
            v-model="selectionForm.courseId" 
            filterable 
            placeholder="请选择课程" 
            @change="handleCourseChange"
            style="width: 100%">
            <el-option
              v-for="item in courseOptions"
              :key="item.courseId"
              :label="`${item.courseId} - ${item.courseName}`"
              :value="item.courseId">
              <span>{{ item.courseId }} - {{ item.courseName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">
                {{ item.teacherName }} | {{ item.credit }}学分
              </span>
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="选课年份" prop="selectionYear">
          <el-date-picker
            v-model="selectionForm.selectionYear"
            type="year"
            value-format="yyyy"
            placeholder="选择年份">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="selectionForm.remark" :rows="3" placeholder="选填"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm('selectionForm')" :loading="loading">提交</el-button>
          <el-button @click="resetForm('selectionForm')">重置</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 预览信息 -->
      <el-card class="preview-card" v-if="showPreview">
        <div slot="header" class="clearfix">
          <span>选课信息预览</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="学生学号">{{ selectionForm.studentId }}</el-descriptions-item>
          <el-descriptions-item label="学生姓名">{{ selectionForm.studentName }}</el-descriptions-item>
          <el-descriptions-item label="课程编号">{{ selectionForm.courseId }}</el-descriptions-item>
          <el-descriptions-item label="课程名称">{{ selectionForm.courseName }}</el-descriptions-item>
          <el-descriptions-item label="授课教师">{{ selectionForm.teacherName }}</el-descriptions-item>
          <el-descriptions-item label="学分">{{ selectionForm.credit }}</el-descriptions-item>
          <el-descriptions-item label="选课年份">{{ selectionForm.selectionYear }}</el-descriptions-item>
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
          { required: true, message: '请选择学生', trigger: 'change' }
        ],
        courseId: [
          { required: true, message: '请选择课程', trigger: 'change' }
        ],
        selectionYear: [
          { required: true, message: '请选择选课年份', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    showPreview() {
      return this.selectionForm.studentId && this.selectionForm.courseId
    }
  },
  created() {
    this.fetchStudents()
    this.fetchCourses()
  },
  methods: {
    ...mapActions('course-selection', ['addCourseSelection']),
    ...mapActions('student', ['getStudents']),
    ...mapActions('course', ['getCourses']),
    
    // 获取学生列表
    fetchStudents() {
      this.getStudents({ size: 1000 })
        .then(data => {
          this.studentOptions = data.records || []
        })
        .catch(() => {
          this.$message.error('获取学生列表失败')
        })
    },
    
    // 获取课程列表
    fetchCourses() {
      this.getCourses({ size: 1000 })
        .then(data => {
          this.courseOptions = data.records || []
        })
        .catch(() => {
          this.$message.error('获取课程列表失败')
        })
    },
    
    // 学生选择变更
    handleStudentChange(studentId) {
      const student = this.studentOptions.find(item => item.studentId === studentId)
      if (student) {
        this.selectionForm.studentName = student.name
      }
    },
    
    // 课程选择变更
    handleCourseChange(courseId) {
      const course = this.courseOptions.find(item => item.courseId === courseId)
      if (course) {
        this.selectionForm.courseName = course.courseName
        this.selectionForm.teacherId = course.teacherId
        this.selectionForm.teacherName = course.teacherName
        this.selectionForm.credit = course.credit
      }
    },
    
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true
          
          this.addCourseSelection(this.selectionForm)
            .then(() => {
              this.$message.success('添加选课成功')
              this.goBack()
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          this.$message.error('请正确填写表单内容')
          return false
        }
      })
    },
    
    // 重置表单
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
    
    // 返回列表
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