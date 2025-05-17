<template>
  <div class="dashboard-container">
    <el-card class="welcome-card">
      <div class="welcome-message">
        <h1>Welcome to SCUT School of Computer Science Course & Grade Management System</h1>
        <p v-if="roleName">Current Role: <span class="role-tag">{{ roleName }}</span></p>
      </div>
    </el-card>

    <!-- Quick Statistics -->
    <el-row :gutter="20" class="dashboard-stats">
      <el-col :span="8">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon">
            <i class="el-icon-user"></i>
          </div>
          <div class="stats-info">
            <div class="stats-title">Total Students</div>
            <div class="stats-value">{{ stats.studentCount || '--' }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon">
            <i class="el-icon-reading"></i>
          </div>
          <div class="stats-info">
            <div class="stats-title">Total Courses</div>
            <div class="stats-value">{{ stats.courseCount || '--' }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon">
            <i class="el-icon-data-line"></i>
          </div>
          <div class="stats-info">
            <div class="stats-title">Average Score</div>
            <div class="stats-value">{{ stats.averageScore || '--' }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Admin Functions -->
    <div v-if="isAdmin" class="dashboard-panels">
      <h2 class="panel-title"><i class="el-icon-menu"></i> System Management</h2>
      <el-row :gutter="20">
        <el-col :span="8" v-for="(item, index) in adminMenus" :key="index">
          <el-card shadow="hover" class="dashboard-card">
            <div class="card-header">
              <i :class="item.icon"></i>
              <span>{{ item.title }}</span>
            </div>
            <div class="dashboard-content">
              <p>{{ item.description }}</p>
              <el-button type="primary" @click="goTo(item.path)">{{ item.buttonText }}</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- Teacher Functions -->
    <div v-if="isTeacher" class="dashboard-panels">
      <h2 class="panel-title"><i class="el-icon-s-management"></i> Teacher Functions</h2>
      <el-row :gutter="20">
        <el-col :span="8" v-for="(item, index) in teacherMenus" :key="index">
          <el-card shadow="hover" class="dashboard-card">
            <div class="card-header">
              <i :class="item.icon"></i>
              <span>{{ item.title }}</span>
            </div>
            <div class="dashboard-content">
              <p>{{ item.description }}</p>
              <el-button type="primary" @click="goTo(item.path)">{{ item.buttonText }}</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- Student Functions -->
    <div v-if="isStudent" class="dashboard-panels">
      <h2 class="panel-title"><i class="el-icon-school"></i> Student Services</h2>
      <el-row :gutter="20">
        <el-col :span="8" v-for="(item, index) in studentMenus" :key="index">
          <el-card shadow="hover" class="dashboard-card">
            <div class="card-header">
              <i :class="item.icon"></i>
              <span>{{ item.title }}</span>
            </div>
            <div class="dashboard-content">
              <p>{{ item.description }}</p>
              <el-button type="primary" @click="goTo(item.path)">{{ item.buttonText }}</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getRole, getRoleName } from '@/utils/auth'
import { isAdmin, isTeacher, isStudent } from '@/utils/auth'
import { getDashboardStats } from '@/api/statistics'

export default {
  name: 'Dashboard',
  data() {
    return {
      role: getRole(),
      roleName: getRoleName(),
      stats: {
        studentCount: 0,
        courseCount: 0,
        averageScore: 0
      },
      adminMenus: [
        {
          title: 'Student Management',
          icon: 'el-icon-user',
          description: 'Manage student information and view student list',
          path: '/student/list',
          buttonText: 'Manage'
        },
        {
          title: 'Teacher Management',
          icon: 'el-icon-s-custom',
          description: 'Manage teacher information and view teacher list',
          path: '/teacher/list',
          buttonText: 'Manage'
        },
        {
          title: 'Course Management',
          icon: 'el-icon-reading',
          description: 'Manage course information and view course list',
          path: '/course/list',
          buttonText: 'Manage'
        },
        {
          title: 'Course Selection',
          icon: 'el-icon-notebook-1',
          description: 'Manage course selection and view selection list',
          path: '/course-selection/list',
          buttonText: 'Manage'
        },
        {
          title: 'Grade Management',
          icon: 'el-icon-s-data',
          description: 'Manage student grades and view grade list',
          path: '/score/list',
          buttonText: 'Manage'
        },
        {
          title: 'Statistics',
          icon: 'el-icon-pie-chart',
          description: 'View school-wide grade statistics and analysis',
          path: '/statistics/all',
          buttonText: 'View Stats'
        }
      ],
      teacherMenus: [
        {
          title: 'Course Selection',
          icon: 'el-icon-notebook-1',
          description: 'View course selections for your taught courses',
          path: '/course-selection/list',
          buttonText: 'View'
        },
        {
          title: 'Grade Management',
          icon: 'el-icon-s-data',
          description: 'Manage student grades and enter scores',
          path: '/score/list',
          buttonText: 'Manage'
        },
        {
          title: 'Class Statistics',
          icon: 'el-icon-s-marketing',
          description: 'View class grade statistics and analysis',
          path: '/statistics/class',
          buttonText: 'View Stats'
        },
        {
          title: 'Course Statistics',
          icon: 'el-icon-pie-chart',
          description: 'View course grade statistics and analysis',
          path: '/statistics/course',
          buttonText: 'View Stats'
        }
      ],
      studentMenus: [
        {
          title: 'My Courses',
          icon: 'el-icon-notebook-1',
          description: 'View your course selection and enrolled courses',
          path: '/course-selection/list',
          buttonText: 'View'
        },
        {
          title: 'My Grades',
          icon: 'el-icon-s-data',
          description: 'View your course grades',
          path: '/score/list',
          buttonText: 'View'
        },
        {
          title: 'Grade Analysis',
          icon: 'el-icon-pie-chart',
          description: 'View your grade analysis and statistics',
          path: '/statistics/student',
          buttonText: 'View Analysis'
        }
      ]
    }
  },
  computed: {
    isAdmin() {
      return isAdmin()
    },
    isTeacher() {
      return isTeacher()
    },
    isStudent() {
      return isStudent()
    }
  },
  mounted() {
    // Get statistics data
    this.fetchDashboardStats()
  },
  methods: {
    goTo(path) {
      this.$router.push(path)
    },
    fetchDashboardStats() {
      this.$message({
        message: '正在加载统计数据...',
        type: 'info',
        duration: 1000
      })
      
      getDashboardStats()
        .then(response => {
          if (response.code === 200 && response.data) {
            this.stats = response.data
          } else {
            this.$message.error('获取统计数据失败')
          }
        })
        .catch(error => {
          console.error('获取Dashboard统计数据失败:', error)
          this.$message.error('获取统计数据失败')
        })
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  color: white;
}

.welcome-message {
  text-align: center;
  padding: 20px 0;
}

.welcome-message h1 {
  font-size: 24px;
  margin-bottom: 10px;
  color: white;
}

.welcome-message p {
  font-size: 16px;
  margin: 10px 0;
}

.role-tag {
  background-color: rgba(255, 255, 255, 0.3);
  padding: 2px 8px;
  border-radius: 4px;
  margin-left: 8px;
}

.dashboard-stats {
  margin-bottom: 30px;
}

.stats-card {
  display: flex;
  align-items: center;
  padding: 20px;
  margin-bottom: 20px;
  height: 100px;
  transition: all 0.3s;
}

.stats-card:hover {
  transform: translateY(-5px);
}

.stats-icon {
  font-size: 36px;
  color: #1890ff;
  margin-right: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: rgba(24, 144, 255, 0.1);
}

.stats-info {
  display: flex;
  flex-direction: column;
}

.stats-title {
  font-size: 16px;
  color: #666;
  margin-bottom: 5px;
}

.stats-value {
  font-size: 28px;
  font-weight: bold;
  color: #1890ff;
}

.panel-title {
  margin: 20px 0;
  font-size: 18px;
  color: #303133;
  font-weight: bold;
  padding-left: 10px;
  border-left: 4px solid #1890ff;
}

.dashboard-panels {
  margin-top: 20px;
}

.dashboard-card {
  height: 180px;
  margin-bottom: 20px;
  transition: all 0.3s;
  overflow: hidden;
}

.dashboard-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.card-header {
  font-size: 18px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.card-header i {
  margin-right: 8px;
}

.dashboard-content {
  padding: 0 20px 20px;
  text-align: center;
}

.dashboard-content p {
  margin-bottom: 15px;
  color: #5a5e66;
  height: 40px;
}

.dashboard-content .el-button {
  width: 100%;
}
</style> 