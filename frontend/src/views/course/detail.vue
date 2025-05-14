<template>
  <div class="container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <span>Course Details</span>
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
              <span class="label">Course ID:</span>
              <span class="content">{{ courseInfo.courseId }}</span>
            </div>
            <div class="info-item">
              <span class="label">Course Name:</span>
              <span class="content">{{ courseInfo.courseName }}</span>
            </div>
            <div class="info-item">
              <span class="label">Instructor:</span>
              <span class="content">{{ courseInfo.teacherName }} ({{ courseInfo.teacherId }})</span>
            </div>
            <div class="info-item">
              <span class="label">Credits:</span>
              <span class="content">{{ courseInfo.credit }}</span>
            </div>
            <div class="info-item">
              <span class="label">Hours:</span>
              <span class="content">{{ courseInfo.hours }}</span>
            </div>
            <div class="info-item">
              <span class="label">Semester:</span>
              <span class="content">{{ courseInfo.semester }}</span>
            </div>
            <div class="info-item">
              <span class="label">Description:</span>
              <span class="content description">{{ courseInfo.description || 'No description available' }}</span>
            </div>
          </el-card>
        </el-col>
        
        <!-- Enrolled Students -->
        <el-col :span="16">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>Enrolled Students</span>
              <span class="student-count">Total: {{ allStudents.length }} students</span>
            </div>
            
            <!-- Student List -->
            <el-table
              v-if="displayStudents.length > 0"
              :data="displayStudents"
              border
              style="width: 100%">
              <el-table-column prop="studentId" label="Student ID" width="120" align="center"></el-table-column>
              <el-table-column prop="name" label="Name" width="120" align="center"></el-table-column>
              <el-table-column prop="gender" label="Gender" width="80" align="center"></el-table-column>
              <el-table-column prop="class" label="Class" width="120" align="center"></el-table-column>
              <el-table-column prop="score" label="Score" width="100" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.score !== null ? scope.row.score : 'Not entered' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="Actions" width="150" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="viewStudentDetail(scope.row)">View Details</el-button>
                  <el-button v-if="isTeacher" type="text" size="small" @click="editScore(scope.row)">Enter Score</el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <el-empty v-else description="No enrolled students"></el-empty>
            
            <!-- Pagination -->
            <el-pagination
              v-if="allStudents.length > 0"
              class="mt-20"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pagination.page"
              :page-sizes="[10, 20, 50]"
              :page-size="pagination.size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="allStudents.length">
            </el-pagination>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'CourseDetail',
  data() {
    return {
      loading: true,
      courseId: '',
      courseInfo: {},
      allStudents: [], // 存储所有学生
      displayStudents: [], // 存储当前页显示的学生
      pagination: {
        page: 1,
        size: 10
      }
    }
  },
  computed: {
    ...mapGetters(['userRole', 'userInfo', 'userRealId', 'userRoleName']),
    isTeacher() {
      return this.userRole === '2' // 2 represents teacher role
    },
    isStudent() {
      return this.userRole === '3' // 3 represents student role
    },
    isAdmin() {
      return this.userRole === '1' // 1 represents admin role
    }
  },
  created() {
    this.courseId = this.$route.params.id
    if (!this.courseId) {
      this.$message.error('Course ID cannot be empty')
      this.goBack()
      return
    }
    this.fetchData()
  },
  methods: {
    ...mapActions({
      getCourse: 'course/getCourseDetail' // 保持原有的action名称，只修改后端接口
    }),
    
    fetchData() {
      this.loading = true
      
      // 使用新的API路径获取课程详情和学生名单
      this.getCourse(this.courseId)
        .then(data => {
          this.courseInfo = data
          // 假设API返回的数据包含students字段，包含所有选择该课程的学生
          this.allStudents = data.students || []
          this.updateDisplayStudents()
        })
        .catch(() => {
          this.$message.error('Failed to get course information')
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    // 前端处理分页
    updateDisplayStudents() {
      const start = (this.pagination.page - 1) * this.pagination.size
      const end = start + this.pagination.size
      this.displayStudents = this.allStudents.slice(start, end)
    },
    
    handleSizeChange(val) {
      this.pagination.size = val
      this.updateDisplayStudents()
    },
    
    handleCurrentChange(val) {
      this.pagination.page = val
      this.updateDisplayStudents()
    },
    
    handleEdit() {
      this.$router.push(`/course/edit/${this.courseId}`)
    },
    
    viewStudentDetail(student) {
      this.$router.push(`/student/detail/${student.studentId}`)
    },
    
    editScore(student) {
      this.$router.push({
        path: '/score/edit',
        query: {
          studentId: student.studentId,
          courseId: this.courseId
        }
      })
    },
    
    goBack() {
      this.$router.push('/course/list')
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
  width: 90px;
  vertical-align: top;
}
.content {
  color: #303133;
  word-break: break-all;
}
.description {
  display: inline-block;
  max-width: calc(100% - 90px);
}
.student-count {
  margin-left: 10px;
  color: #909399;
  font-size: 14px;
}
.mb-20 {
  margin-bottom: 20px;
}
.mt-20 {
  margin-top: 20px;
}
</style> 