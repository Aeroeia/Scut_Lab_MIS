<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Course List</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleCreate">Add Course</el-button>
      </div>
      
      <!-- Search Area -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="Course ID">
          <el-input v-model="searchForm.courseId" placeholder="Enter course ID" clearable style="width: 150px"></el-input>
        </el-form-item>
        <el-form-item label="Course Name" >
          <el-input v-model="searchForm.courseName" placeholder="Enter course name" clearable style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="Teacher">
          <el-input v-model="searchForm.teacherName" placeholder="Enter teacher name" clearable style="width: 150px"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="resetSearch">Reset</el-button>
        </el-form-item>
      </el-form>
      
      <!-- Data Table -->
      <el-table v-loading="loading" :data="courseList" border style="width: 100%">
        <el-table-column prop="courseId" label="Course ID" width="120" align="center"></el-table-column>
        <el-table-column prop="courseName" label="Course Name" min-width="150" align="center" width="200"></el-table-column>
        <el-table-column prop="teacherId" label="Teacher ID" width="120" align="center"></el-table-column>
        <el-table-column prop="teacherName" label="Teacher Name" width="120" align="center"></el-table-column>
        <el-table-column prop="credit" label="Credits" width="80" align="center"></el-table-column>
        <el-table-column prop="hours" label="Hours" width="80" align="center"></el-table-column>
        <el-table-column prop="semester" label="Semester" width="120" align="center"></el-table-column>
        <el-table-column label="Actions" align="center" width="180">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleDetail(scope.row)">Details</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button type="text" size="small" class="danger-text" @click="handleDelete(scope.row)">Delete</el-button>
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
import { mapActions } from 'vuex'

export default {
  name: 'CourseList',
  data() {
    return {
      loading: false,
      searchForm: {
        courseId: '',
        courseName: '',
        teacherName: ''
      },
      courseList: [],
      pagination: {
        page: 1,
        size: 10
      },
      total: 0
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    ...mapActions('course', ['getCourses', 'deleteCourse']),
    
    fetchData() {
      this.loading = true
      const params = {
        page: this.pagination.page,
        size: this.pagination.size,
        courseId: this.searchForm.courseId || undefined,
        courseName: this.searchForm.courseName || undefined,
        teacherName: this.searchForm.teacherName || undefined
      }
      
      this.getCourses(params)
        .then(data => {
          this.courseList = data.records || []
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
      this.searchForm = {
        courseId: '',
        courseName: '',
        teacherName: ''
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
      this.$router.push('/course/create')
    },
    
    handleEdit(row) {
      this.$router.push({
        path: `/course/edit/${row.courseId}`,
        query: { teacherId: row.teacherId }
      })
    },
    
    handleDetail(row) {
      this.$router.push({
        path: `/course/detail/${row.courseId}`,
        query: { teacherId: row.teacherId }
      })
    },
    
    handleDelete(row) {
      this.$confirm('Are you sure you want to delete this course?', 'Confirm', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.deleteCourse(row.courseId).then(() => {
          this.$message.success('Deleted successfully')
          this.fetchData()
        })
      }).catch(() => {
        this.$message.info('Delete cancelled')
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