<template>
  <div class="container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <span>编辑教师</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回列表</el-button>
      </div>
      
      <el-form :model="teacherForm" :rules="rules" ref="teacherForm" label-width="100px">
        <el-form-item label="工号" prop="teacherId">
          <el-input v-model="teacherForm.teacherId" placeholder="请输入工号" disabled></el-input>
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
        
        <el-form-item label="重置密码" prop="resetPassword">
          <el-switch v-model="resetPassword"></el-switch>
          <span class="tips">开启后将重置为默认密码：123456</span>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'TeacherEdit',
  data() {
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
      loading: true,
      submitting: false,
      resetPassword: false,
      teacherForm: {
        teacherId: '',
        name: '',
        gender: '男',
        title: '',
        department: '',
        email: '',
        phone: '',
        hireDate: ''
      },
      rules: {
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
        ]
      }
    }
  },
  created() {
    this.fetchTeacherData()
  },
  methods: {
    ...mapActions('teacher', ['getTeacher', 'updateTeacher']),
    
    fetchTeacherData() {
      const teacherId = this.$route.params.id
      if (!teacherId) {
        this.$message.error('教师ID不能为空')
        this.goBack()
        return
      }
      
      this.loading = true
      this.getTeacher(teacherId)
        .then(data => {
          this.teacherForm = { ...data }
        })
        .catch(() => {
          this.$message.error('获取教师信息失败')
          this.goBack()
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    submitForm() {
      this.$refs.teacherForm.validate(valid => {
        if (valid) {
          this.submitting = true
          
          // 添加重置密码选项
          const teacherData = { ...this.teacherForm }
          if (this.resetPassword) {
            teacherData.resetPassword = true
          }
          
          this.updateTeacher(teacherData)
            .then(() => {
              this.$message.success('更新教师信息成功')
              this.goBack()
            })
            .finally(() => {
              this.submitting = false
            })
        } else {
          this.$message.error('请正确填写表单内容')
          return false
        }
      })
    },
    
    resetForm() {
      this.fetchTeacherData()
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
.tips {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}
</style> 