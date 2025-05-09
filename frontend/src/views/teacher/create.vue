<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>添加教师</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回列表</el-button>
      </div>
      
      <el-form :model="teacherForm" :rules="rules" ref="teacherForm" label-width="100px">
        <el-form-item label="工号" prop="teacherId">
          <el-input v-model="teacherForm.teacherId" placeholder="请输入工号"></el-input>
        </el-form-item>
        
        <el-form-item label="姓名" prop="name">
          <el-input v-model="teacherForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="teacherForm.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="职称" prop="title">
          <el-select v-model="teacherForm.title" placeholder="请选择职称">
            <el-option label="讲师" value="讲师"></el-option>
            <el-option label="副教授" value="副教授"></el-option>
            <el-option label="教授" value="教授"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="所属院系" prop="department">
          <el-input v-model="teacherForm.department" placeholder="请输入所属院系"></el-input>
        </el-form-item>
        
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="teacherForm.email" placeholder="请输入电子邮箱"></el-input>
        </el-form-item>
        
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="teacherForm.phone" placeholder="请输入手机号码"></el-input>
        </el-form-item>
        
        <el-form-item label="入职日期" prop="hireDate">
          <el-date-picker
            v-model="teacherForm.hireDate"
            type="date"
            placeholder="选择入职日期"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="登录密码" prop="password">
          <el-input v-model="teacherForm.password" type="password" placeholder="请输入登录密码"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="loading">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
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
    // 校验工号
    const validateTeacherId = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入工号'))
      } else if (!/^\d{8}$/.test(value)) {
        callback(new Error('工号必须为8位数字'))
      } else {
        callback()
      }
    }
    
    // 校验邮箱
    const validateEmail = (rule, value, callback) => {
      if (!value) {
        callback()
      } else if (!/^[\w.-]+@[\w.-]+\.\w+$/.test(value)) {
        callback(new Error('请输入正确的邮箱格式'))
      } else {
        callback()
      }
    }
    
    // 校验手机号
    const validatePhone = (rule, value, callback) => {
      if (!value) {
        callback()
      } else if (!/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号格式'))
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
        password: '123456' // 默认密码
      },
      rules: {
        teacherId: [
          { required: true, message: '请输入工号', trigger: 'blur' },
          { validator: validateTeacherId, trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        title: [
          { required: true, message: '请选择职称', trigger: 'change' }
        ],
        department: [
          { required: true, message: '请输入所属院系', trigger: 'blur' }
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ],
        phone: [
          { validator: validatePhone, trigger: 'blur' }
        ],
        hireDate: [
          { required: true, message: '请选择入职日期', trigger: 'change' }
        ],
        password: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
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
              this.$message.success('添加教师成功')
              this.goBack()
            })
            .finally(() => {
              this.loading = false
            })
        } else {
          this.$message.error('请正确填写表单内容')
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