<template>
  <div class="container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <span>Teacher Details</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">Back to List</el-button>
      </div>
      
      <el-row :gutter="20" v-if="!loading">
        <!-- Basic Information -->
        <el-col :span="8">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>Basic Information</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="handleEdit">Edit</el-button>
            </div>
            <div class="info-item">
              <span class="label">ID:</span>
              <span class="content">{{ teacherInfo.teacherId }}</span>
            </div>
            <div class="info-item">
              <span class="label">Name:</span>
              <span class="content">{{ teacherInfo.name }}</span>
            </div>
            <div class="info-item">
              <span class="label">Gender:</span>
              <span class="content">{{ teacherInfo.gender }}</span>
            </div>
            <div class="info-item">
              <span class="label">Title:</span>
              <span class="content">{{ teacherInfo.title }}</span>
            </div>
            <div class="info-item">
              <span class="label">Department:</span>
              <span class="content">{{ teacherInfo.department }}</span>
            </div>
            <div class="info-item">
              <span class="label">Email:</span>
              <span class="content">{{ teacherInfo.email || 'Not set' }}</span>
            </div>
            <div class="info-item">
              <span class="label">Phone:</span>
              <span class="content">{{ teacherInfo.phone || 'Not set' }}</span>
            </div>
            <div class="info-item">
              <span class="label">Hire Date:</span>
              <span class="content">{{ teacherInfo.hireDate }}</span>
            </div>
          </el-card>
        </el-col>
        
        <!-- Teaching Courses -->
        <el-col :span="16">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>Teaching Courses</span>
            </div>
            
            <el-table
              v-if="teacherCourses.length > 0"
              :data="teacherCourses"
              border
              style="width: 100%">
              <el-table-column prop="courseId" label="Course ID" width="120" align="center"></el-table-column>
              <el-table-column prop="courseName" label="Course Name" min-width="150" align="center"></el-table-column>
              <el-table-column prop="credit" label="Credits" width="80" align="center"></el-table-column>
              <el-table-column prop="hours" label="Hours" width="80" align="center"></el-table-column>
              <el-table-column prop="semester" label="Semester" width="120" align="center"></el-table-column>
              <el-table-column label="Actions" width="150" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="viewStudents(scope.row)">View Students</el-button>
                  <el-button type="text" size="small" @click="viewScores(scope.row)">View Scores</el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <el-empty v-else description="No courses available"></el-empty>
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
      this.$message.error('Teacher ID cannot be empty')
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
      
      // Get teacher information
      const teacherPromise = this.getTeacher(this.teacherId)
        .then(data => {
          this.teacherInfo = data
        })
      
      // Get teacher courses
      const coursesPromise = this.getTeacherCourses(this.teacherId)
        .then(data => {
          this.teacherCourses = data || []
        })
      
      // Wait for all requests to complete
      Promise.all([teacherPromise, coursesPromise])
        .catch(() => {
          this.$message.error('Failed to get teacher information')
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