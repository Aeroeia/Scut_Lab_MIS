<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Profile Information</span>
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
            <el-descriptions title="Basic Information" :column="2" border>
              <el-descriptions-item label="ID">{{ userInfo.realId }}</el-descriptions-item>
              <el-descriptions-item label="Role">{{ userInfo.roleName }}</el-descriptions-item>
              <el-descriptions-item label="Username">{{ userInfo.username }}</el-descriptions-item>
              <el-descriptions-item label="Login Time">{{ loginTime }}</el-descriptions-item>
            </el-descriptions>
            
            <el-divider></el-divider>
            
            <el-form ref="passwordForm" :model="passwordForm" :rules="passwordRules" label-width="120px" class="password-form">
              <h3>Change Password</h3>
              <el-form-item label="Old Password" prop="oldPassword">
                <el-input v-model="passwordForm.oldPassword" type="password" show-password></el-input>
              </el-form-item>
              <el-form-item label="New Password" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" type="password" show-password></el-input>
              </el-form-item>
              <el-form-item label="Confirm Password" prop="confirmPassword">
                <el-input v-model="passwordForm.confirmPassword" type="password" show-password></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm">Update Password</el-button>
                <el-button @click="resetForm">Reset</el-button>
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
    // Validate password confirmation
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('Passwords do not match'))
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
          { required: true, message: 'Please enter your old password', trigger: 'blur' },
          { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: 'Please enter your new password', trigger: 'blur' },
          { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: 'Please confirm your new password', trigger: 'blur' },
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
    // Submit password change form
    submitForm() {
      this.$refs.passwordForm.validate(valid => {
        if (valid) {
          this.$message({
            message: 'Password updated successfully',
            type: 'success'
          })
          this.resetForm()
        } else {
          return false
        }
      })
    },
    
    // Reset form
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