<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Course Selection List</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleCreate" v-if="isAdmin">Add Course Selection</el-button>
      </div>
      
      <!-- Search Area -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="Student ID">
          <el-input v-model="searchForm.studentId" placeholder="Enter student ID" clearable></el-input>
        </el-form-item>
        <el-form-item label="Student Name">
          <el-input v-model="searchForm.studentName" placeholder="Enter student name" clearable></el-input>
        </el-form-item>
        <el-form-item label="Course ID">
          <el-input v-model="searchForm.courseId" placeholder="Enter course ID" clearable></el-input>
        </el-form-item>
        <el-form-item label="Course Name">
          <el-input v-model="searchForm.courseName" placeholder="Enter course name" clearable></el-input>
        </el-form-item>
        <el-form-item label="Selection Year">
          <el-date-picker
            v-model="searchForm.selectionYear"
            type="year"
            value-format="yyyy"
            placeholder="Select year">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="resetSearch">Reset</el-button>
        </el-form-item>
      </el-form>
      
      <!-- Data Table -->
      <el-table v-loading="loading" :data="selectionList" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="studentId" label="Student ID" width="120" align="center"></el-table-column>
        <el-table-column prop="studentName" label="Student Name" width="120" align="center"></el-table-column>
        <el-table-column prop="courseId" label="Course ID" width="120" align="center"></el-table-column>
        <el-table-column prop="courseName" label="Course Name" min-width="150" align="center"></el-table-column>
        <el-table-column prop="teacherName" label="Teacher" width="120" align="center"></el-table-column>
        <el-table-column prop="selectionYear" label="Selection Year" width="120" align="center"></el-table-column>
        <el-table-column prop="score" label="Score" width="80" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.score !== null ? scope.row.score : 'Not entered' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Actions" align="center" width="200">
          <template slot-scope="scope">
            <el-button 
              type="text" 
              size="small" 
              v-if="isTeacher && scope.row.score === null"
              @click="handleInputScore(scope.row)">
              Enter Score
            </el-button>
            <el-button 
              type="text" 
              size="small" 
              class="danger-text" 
              v-if="isAdmin"
              @click="handleDelete(scope.row)">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- Pagination -->
      <el-pagination
        class="mt-20"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'CourseSelectionList',
  data() {
    return {
      loading: false,
      searchForm: {
        studentId: '',
        studentName: '',
        courseId: '',
        courseName: '',
        selectionYear: ''
      },
      selectionList: [],
      pagination: {
        page: 1,
        size: 10
      },
      total: 0
    }
  },
  computed: {
    ...mapGetters(['userRole', 'userInfo', 'userRealId']),
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
    // 如果是从教师详情页传入了参数，优先使用参数
    if (this.$route.query.courseId) {
      this.searchForm.courseId = this.$route.query.courseId
    }
    if (this.$route.query.teacherId) {
      this.searchForm.teacherId = this.$route.query.teacherId
    }
    
    this.fetchData()
  },
  methods: {
    ...mapActions({
      getCourseSelections: 'courseSelection/getCourseSelections', 
      deleteCourseSelection: 'courseSelection/deleteCourseSelection'
    }),
    
    fetchData() {
      this.loading = true
      const params = {
        page: this.pagination.page,
        size: this.pagination.size,
        studentId: this.searchForm.studentId || undefined,
        studentName: this.searchForm.studentName || undefined,
        courseId: this.searchForm.courseId || undefined,
        courseName: this.searchForm.courseName || undefined,
        selectionYear: this.searchForm.selectionYear || undefined,
        teacherId: this.searchForm.teacherId || undefined
      }
      
      this.getCourseSelections(params)
        .then(data => {
          this.selectionList = data.records || []
          this.total = data.total || 0
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    handleSearch() {
      this.pagination.page = 1
      this.fetchData()
    },
    
    resetSearch() {
      // 保留可能从其他页面传入的查询参数
      const teacherId = this.searchForm.teacherId
      const courseId = this.searchForm.courseId
      
      this.searchForm = {
        studentId: '',
        studentName: '',
        courseId: courseId || '',
        courseName: '',
        selectionYear: '',
        teacherId: teacherId || ''
      }
      
      this.pagination.page = 1
      this.fetchData()
    },
    
    handleSizeChange(val) {
      this.pagination.size = val
      this.fetchData()
    },
    
    handleCurrentChange(val) {
      this.pagination.page = val
      this.fetchData()
    },
    
    handleCreate() {
      this.$router.push('/course-selection/create')
    },
    
    handleInputScore(row) {
      this.$router.push({
        path: '/score/edit',
        query: {
          studentId: row.studentId,
          courseId: row.courseId
        }
      })
    },

    handleDelete(row) {
      this.$confirm('Are you sure you want to delete this course selection?', 'Confirmation', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.deleteCourseSelection(row.id).then(() => {
          this.$message.success('Successfully deleted')
          this.fetchData()
        })
      }).catch(() => {
        this.$message.info('Deletion cancelled')
      })
    }
  }
}
</script>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.danger-text {
  color: #F56C6C;
}
</style> 