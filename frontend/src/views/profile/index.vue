<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>个人信息</span>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="hover" class="avatar-card">
            <div class="avatar-wrapper">
              <el-avatar :size="120" :src="userAvatar" icon="el-icon-user-solid"></el-avatar>
            </div>
            <div class="user-info">
              <h3>{{ userInfo.name || userInfo.realId }}</h3>
              <p>{{ userInfo.roleName }}</p>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="16">
          <el-card shadow="hover">
            <el-descriptions title="基本信息" :column="2" border>
              <el-descriptions-item label="ID">{{ userInfo.realId }}</el-descriptions-item>
              <el-descriptions-item label="角色">{{ userInfo.roleName }}</el-descriptions-item>
              <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
              <el-descriptions-item label="登录时间">{{ loginTime }}</el-descriptions-item>
            </el-descriptions>
            
            <el-divider></el-divider>
            
            <el-form ref="passwordForm" :model="passwordForm" :rules="passwordRules" label-width="100px" class="password-form">
              <h3>修改密码</h3>
              <el-form-item label="旧密码" prop="oldPassword">
                <el-input v-model="passwordForm.oldPassword" type="password" show-password></el-input>
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" type="password" show-password></el-input>
              </el-form-item>
              <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="passwordForm.confirmPassword" type="password" show-password></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm">修改密码</el-button>
                <el-button @click="resetForm">重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Profile',
  data() {
    // 验证密码是否一致
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    
    return {
      userAvatar: '',
      loginTime: new Date().toLocaleString(),
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入旧密码', trigger: 'blur' },
          { min: 6, message: '密码长度不少于6位', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不少于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  methods: {
    // 提交修改密码表单
    submitForm() {
      this.$refs.passwordForm.validate(valid => {
        if (valid) {
          this.$message({
            message: '密码修改成功',
            type: 'success'
          })
          this.resetForm()
        } else {
          return false
        }
      })
    },
    
    // 重置表单
    resetForm() {
      this.$refs.passwordForm.resetFields()
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20px;
}
.avatar-card {
  text-align: center;
  padding: 20px;
}
.avatar-wrapper {
  margin-bottom: 20px;
}
.user-info {
  margin-top: 20px;
}
.user-info h3 {
  margin: 10px 0;
  font-size: 18px;
}
.user-info p {
  color: #909399;
}
.password-form {
  max-width: 500px;
  margin-top: 20px;
}
</style> 