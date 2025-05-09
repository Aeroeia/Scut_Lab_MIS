<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>添加课程</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回列表</el-button>
      </div>
      
      <el-form :model="courseForm" :rules="rules" ref="courseForm" label-width="100px" class="courseForm">
        <el-form-item label="课程编号" prop="courseId">
          <el-input v-model="courseForm.courseId" placeholder="请输入课程编号"></el-input>
        </el-form-item>
        
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="courseForm.courseName" placeholder="请输入课程名称"></el-input>
        </el-form-item>
        
        <el-form-item label="授课教师" prop="teacherId">
          <el-select v-model="courseForm.teacherId" placeholder="请选择授课教师" filterable @change="handleTeacherChange">
            <el-option
              v-for="item in teacherOptions"
              :key="item.teacherId"
              :label="item.name"
              :value="item.teacherId">
              <span>{{ item.teacherId }} - {{ item.name }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="学分" prop="credit">
          <el-input-number v-model="courseForm.credit" :min="1" :max="10" placeholder="请输入学分"></el-input-number>
        </el-form-item>
        
        <el-form-item label="学时" prop="hours">
          <el-input-number v-model="courseForm.hours" :min="8" :max="120" placeholder="请输入学时"></el-input-number>
        </el-form-item>
        
        <el-form-item label="开课学期" prop="semester">
          <el-select v-model="courseForm.semester" placeholder="请选择开课学期">
            <el-option label="2023-2024学年第一学期" value="2023-2024-1"></el-option>
            <el-option label="2023-2024学年第二学期" value="2023-2024-2"></el-option>
            <el-option label="2024-2025学年第一学期" value="2024-2025-1"></el-option>
            <el-option label="2024-2025学年第二学期" value="2024-2025-2"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="课程描述" prop="description">
          <el-input type="textarea" v-model="courseForm.description" placeholder="请输入课程描述" :rows="4"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm('courseForm')" :loading="loading">保存</el-button>
          <el-button @click="resetForm('courseForm')">重置</el-button>
          <el-button @click="goBack">返回</el-button>
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
    // 验证课程编号是否为8位数字
    const validateCourseId = (rule, value, callback) => {
      if (value.length !== 8) {
        callback(new Error('课程编号必须为8位'))
      } else if (!/^\d+$/.test(value)) {
        callback(new Error('课程编号必须为数字'))
      } else {
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
          { required: true, message: '请输入课程编号', trigger: 'blur' },
          { validator: validateCourseId, trigger: 'blur' }
        ],
        courseName: [
          { required: true, message: '请输入课程名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        teacherId: [
          { required: true, message: '请选择授课教师', trigger: 'change' }
        ],
        credit: [
          { required: true, message: '请输入学分', trigger: 'blur' },
          { type: 'number', min: 1, max: 10, message: '学分在1-10之间', trigger: 'blur' }
        ],
        hours: [
          { required: true, message: '请输入学时', trigger: 'blur' },
          { type: 'number', min: 8, max: 120, message: '学时在8-120之间', trigger: 'blur' }
        ],
        semester: [
          { required: true, message: '请选择开课学期', trigger: 'change' }
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
    
    // 获取教师列表
    fetchTeachers() {
      this.getTeachers({ size: 1000 })
        .then(data => {
          this.teacherOptions = data.records || []
        })
        .catch(() => {
          this.$message.error('获取教师列表失败')
        })
    },
    
    // 根据教师ID设置教师姓名
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
              this.$message.success('添加成功')
              this.goBack()
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          this.$message.error('请正确填写表单')
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