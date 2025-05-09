<template>
  <div class="dashboard-container">
    <el-card class="welcome-card">
      <div class="welcome-message">
        <h1>欢迎使用华南理工大学计算机学院课程与成绩管理系统</h1>
        <p v-if="roleName">当前角色：<span class="role-tag">{{ roleName }}</span></p>
      </div>
    </el-card>

    <!-- 快速统计信息 -->
    <el-row :gutter="20" class="dashboard-stats">
      <el-col :span="8">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon">
            <i class="el-icon-user"></i>
          </div>
          <div class="stats-info">
            <div class="stats-title">学生总数</div>
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
            <div class="stats-title">课程总数</div>
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
            <div class="stats-title">平均成绩</div>
            <div class="stats-value">{{ stats.averageScore || '--' }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 管理员功能模块 -->
    <div v-if="isAdmin" class="dashboard-panels">
      <h2 class="panel-title"><i class="el-icon-menu"></i> 系统管理功能</h2>
      <el-row :gutter="20">
        <el-col :span="8" v-for="(item, index) in adminMenus" :key="index">
          <el-card shadow="hover" class="dashboard-card">
            <div class="card-header">
              <i :class="item.icon"></i>
              <span>{{ item.title }}</span>
            </div>
            <div class="dashboard-content">
              <p>{{ item.description }}</p>
              <el-button type="primary" @click="goTo(item.path)">进入管理</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 教师功能模块 -->
    <div v-if="isTeacher" class="dashboard-panels">
      <h2 class="panel-title"><i class="el-icon-s-management"></i> 教师管理功能</h2>
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

    <!-- 学生功能模块 -->
    <div v-if="isStudent" class="dashboard-panels">
      <h2 class="panel-title"><i class="el-icon-school"></i> 学生服务功能</h2>
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
          title: '学生管理',
          icon: 'el-icon-user',
          description: '管理学生信息、查看学生列表',
          path: '/student/list',
          buttonText: '进入管理'
        },
        {
          title: '教师管理',
          icon: 'el-icon-s-custom',
          description: '管理教师信息、查看教师列表',
          path: '/teacher/list',
          buttonText: '进入管理'
        },
        {
          title: '课程管理',
          icon: 'el-icon-reading',
          description: '管理课程信息、查看课程列表',
          path: '/course/list',
          buttonText: '进入管理'
        },
        {
          title: '选课管理',
          icon: 'el-icon-notebook-1',
          description: '管理选课信息、查看选课列表',
          path: '/course-selection/list',
          buttonText: '进入管理'
        },
        {
          title: '成绩管理',
          icon: 'el-icon-s-data',
          description: '管理学生成绩、查看成绩列表',
          path: '/score/list',
          buttonText: '进入管理'
        },
        {
          title: '统计分析',
          icon: 'el-icon-pie-chart',
          description: '查看全校成绩统计和分析',
          path: '/statistics/all',
          buttonText: '查看统计'
        }
      ],
      teacherMenus: [
        {
          title: '选课管理',
          icon: 'el-icon-notebook-1',
          description: '查看您所教授课程的选课情况',
          path: '/course-selection/list',
          buttonText: '查看选课'
        },
        {
          title: '成绩管理',
          icon: 'el-icon-s-data',
          description: '管理学生成绩、录入成绩',
          path: '/score/list',
          buttonText: '成绩管理'
        },
        {
          title: '班级统计',
          icon: 'el-icon-s-marketing',
          description: '查看班级成绩统计和分析',
          path: '/statistics/class',
          buttonText: '查看统计'
        },
        {
          title: '课程统计',
          icon: 'el-icon-pie-chart',
          description: '查看课程成绩统计和分析',
          path: '/statistics/course',
          buttonText: '查看统计'
        }
      ],
      studentMenus: [
        {
          title: '我的选课',
          icon: 'el-icon-notebook-1',
          description: '查看您的选课信息和已选课程',
          path: '/course-selection/list',
          buttonText: '查看选课'
        },
        {
          title: '我的成绩',
          icon: 'el-icon-s-data',
          description: '查看您的课程成绩信息',
          path: '/score/list',
          buttonText: '查看成绩'
        },
        {
          title: '成绩分析',
          icon: 'el-icon-pie-chart',
          description: '查看您的成绩分析和统计',
          path: '/statistics/student',
          buttonText: '查看分析'
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
    // 获取统计数据
    this.fetchDashboardStats()
  },
  methods: {
    goTo(path) {
      this.$router.push(path)
    },
    fetchDashboardStats() {
      // 这里应该调用后端API获取统计数据
      // 为简化示例，使用模拟数据
      setTimeout(() => {
        this.stats = {
          studentCount: 1200,
          courseCount: 86,
          averageScore: 82.5
        }
      }, 500)
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