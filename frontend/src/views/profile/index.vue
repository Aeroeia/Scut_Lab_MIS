<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Profile Information</span>
      </div>
      
      <div v-if="userInfoLoaded" class="content-centered">
        <el-card shadow="hover" class="profile-card">
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
            <el-form-item label="Confirm" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm" :loading="loading">Update Password</el-button>
              <el-button @click="resetForm">Reset</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
      
      <div v-else class="loading-container">
        <el-skeleton :rows="6" animated />
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

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
      userInfoLoaded: false,
      userAvatar: '',
      loginTime: new Date().toLocaleString(),
      loading: false,
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
    ...mapGetters(['userInfo'])
  },
  created() {
    // Ensure user information is loaded
    this.checkUserInfo()
  },
  methods: {
    ...mapActions('user', ['getUserInfo', 'updatePassword']),
    
    // Check if user information is loaded
    checkUserInfo() {
      console.log('Current userInfo:', this.userInfo)
      // First check if userInfo is already valid
      if (this.userInfo && this.userInfo.realId) {
        this.userInfoLoaded = true
      } else {
        // If there's no valid user info, try to load it
        this.getUserInfo()
          .then((data) => {
            console.log('Loaded user info:', data)
            this.userInfoLoaded = true
            this.$message({
              message: '用户信息加载成功',
              type: 'success'
            })
          })
          .catch(error => {
            console.error('Failed to load user information', error)
            this.$message({
              message: '加载用户信息失败，请重新登录',
              type: 'error'
            })
          })
      }
    },
    
    // Submit password change form
    submitForm() {
      this.$refs.passwordForm.validate(valid => {
        if (valid) {
          this.loading = true;
          
          // 调用store的action修改密码
          const passwordData = {
            password: this.passwordForm.newPassword
          };
          
          this.updatePassword(passwordData)
            .then(response => {
          this.$message({
                message: '密码修改成功',
            type: 'success'
              });
              this.resetForm();
            })
            .catch(error => {
              this.$message({
                message: '密码修改失败: ' + (error.message || '未知错误'),
                type: 'error'
              });
            })
            .finally(() => {
              this.loading = false;
            });
        } else {
          return false;
        }
      });
    },
    
    // Reset form
    resetForm() {
      this.$refs.passwordForm.resetFields();
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20px;
}
.content-centered {
  display: flex;
  justify-content: center;
  width: 100%;
}
.profile-card {
  width: 80%;
  max-width: 800px;
}
.password-form {
  max-width: 500px;
  margin: 20px auto;
}
.loading-container {
  padding: 20px;
  display: flex;
  justify-content: center;
}
</style> 