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
              <span class="student-count">Total: {{ studentCount }} students</span>
            </div>
            
            <!-- Student Search -->
            <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
              <el-form-item label="Student ID">
                <el-input v-model="searchForm.studentId" placeholder="Enter Student ID" clearable size="small"></el-input>
              </el-form-item>
              <el-form-item label="Name">
                <el-input v-model="searchForm.name" placeholder="Enter Name" clearable size="small"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" size="small" @click="searchStudents">Search</el-button>
                <el-button size="small" @click="resetSearch">Reset</el-button>
              </el-form-item>
            </el-form>
            
            <!-- Student List -->
            <el-table
              v-if="students.length > 0"
              :data="students"
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
              v-if="students.length > 0"
              class="mt-20"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pagination.page"
              :page-sizes="[10, 20, 50]"
              :page-size="pagination.size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="studentCount">
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
      students: [],
      studentCount: 0,
      searchForm: {
        studentId: '',
        name: ''
      },
      pagination: {
        page: 1,
        size: 10
      }
    }
  },
  computed: {
    ...mapGetters(['userRole']),
    isTeacher() {
      return this.userRole === '2' // 2 represents teacher role
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
      getCourseDetail: 'course/getCourseDetail',
      getCourseStudents: 'course/getCourseStudents'
    }),
    
    fetchData() {
      this.loading = true
      
      // Get course information
      const coursePromise = this.getCourseDetail(this.courseId)
        .then(data => {
          this.courseInfo = data
        })
      
      // Get enrolled students
      const studentsPromise = this.fetchStudents()
      
      // Wait for all requests to complete
      Promise.all([coursePromise, studentsPromise])
        .catch(() => {
          this.$message.error('Failed to get course information')
          this.goBack()
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    fetchStudents() {
      const params = {
        page: this.pagination.page,
        size: this.pagination.size,
        studentId: this.searchForm.studentId || undefined,
        name: this.searchForm.name || undefined
      }
      
      return this.getCourseStudents({ courseId: this.courseId, params })
        .then(data => {
          this.students = data.students || []
          this.studentCount = data.total || 0
        })
    },
    
    searchStudents() {
      this.pagination.page = 1
      this.fetchStudents()
    },
    
    resetSearch() {
      this.searchForm = {
        studentId: '',
        name: ''
      }
      this.pagination.page = 1
      this.fetchStudents()
    },
    
    handleSizeChange(val) {
      this.pagination.size = val
      this.fetchStudents()
    },
    
    handleCurrentChange(val) {
      this.pagination.page = val
      this.fetchStudents()
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