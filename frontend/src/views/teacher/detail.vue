<template>
  <div class="container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <span>教师详情</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回列表</el-button>
      </div>
      
      <el-row :gutter="20" v-if="!loading">
        <!-- 基本信息 -->
        <el-col :span="8">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>基本信息</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="handleEdit">编辑</el-button>
            </div>
            <div class="info-item">
              <span class="label">工号：</span>
              <span class="content">{{ teacherInfo.teacherId }}</span>
            </div>
            <div class="info-item">
              <span class="label">姓名：</span>
              <span class="content">{{ teacherInfo.name }}</span>
            </div>
            <div class="info-item">
              <span class="label">性别：</span>
              <span class="content">{{ teacherInfo.gender }}</span>
            </div>
            <div class="info-item">
              <span class="label">职称：</span>
              <span class="content">{{ teacherInfo.title }}</span>
            </div>
            <div class="info-item">
              <span class="label">所属院系：</span>
              <span class="content">{{ teacherInfo.department }}</span>
            </div>
            <div class="info-item">
              <span class="label">电子邮箱：</span>
              <span class="content">{{ teacherInfo.email || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="label">手机号码：</span>
              <span class="content">{{ teacherInfo.phone || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="label">入职日期：</span>
              <span class="content">{{ teacherInfo.hireDate }}</span>
            </div>
          </el-card>
        </el-col>
        
        <!-- 教授课程 -->
        <el-col :span="16">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>教授课程</span>
            </div>
            
            <el-table
              v-if="teacherCourses.length > 0"
              :data="teacherCourses"
              border
              style="width: 100%">
              <el-table-column prop="courseId" label="课程编号" width="120" align="center"></el-table-column>
              <el-table-column prop="courseName" label="课程名称" min-width="150" align="center"></el-table-column>
              <el-table-column prop="credit" label="学分" width="80" align="center"></el-table-column>
              <el-table-column prop="hours" label="学时" width="80" align="center"></el-table-column>
              <el-table-column prop="semester" label="开课学期" width="120" align="center"></el-table-column>
              <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="viewStudents(scope.row)">查看学生</el-button>
                  <el-button type="text" size="small" @click="viewScores(scope.row)">查看成绩</el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <el-empty v-else description="暂无教授课程"></el-empty>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'TeacherDetail',
  data() {
    return {
      loading: true,
      teacherId: '',
      teacherInfo: {},
      teacherCourses: []
    }
  },
  created() {
    this.teacherId = this.$route.params.id
    if (!this.teacherId) {
      this.$message.error('教师ID不能为空')
      this.goBack()
      return
    }
    this.fetchData()
  },
  methods: {
    ...mapActions({
      getTeacher: 'teacher/getTeacher',
      getTeacherCourses: 'teacher/getTeacherCourses'
    }),
    
    fetchData() {
      this.loading = true
      
      // 获取教师信息
      const teacherPromise = this.getTeacher(this.teacherId)
        .then(data => {
          this.teacherInfo = data
        })
      
      // 获取教师课程
      const coursesPromise = this.getTeacherCourses(this.teacherId)
        .then(data => {
          this.teacherCourses = data || []
        })
      
      // 等待所有请求完成
      Promise.all([teacherPromise, coursesPromise])
        .catch(() => {
          this.$message.error('获取教师信息失败')
          this.goBack()
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    handleEdit() {
      this.$router.push(`/teacher/edit/${this.teacherId}`)
    },
    
    viewStudents(course) {
      this.$router.push({
        path: '/course-selection/list',
        query: {
          courseId: course.courseId,
          teacherId: this.teacherId
        }
      })
    },
    
    viewScores(course) {
      this.$router.push({
        path: '/score/list',
        query: {
          courseId: course.courseId,
          teacherId: this.teacherId
        }
      })
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
.box-card {
  margin-bottom: 20px;
}
.info-item {
  margin-bottom: 15px;
  line-height: 20px;
}
.label {
  color: #606266;
  font-weight: bold;
  margin-right: 5px;
  display: inline-block;
  width: 80px;
}
.content {
  color: #303133;
}
</style> 