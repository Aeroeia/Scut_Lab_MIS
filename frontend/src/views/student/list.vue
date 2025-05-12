<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Student List</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleCreate">Add Student</el-button>
      </div>
      
      <!-- Search Area -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="Student ID">
          <el-input v-model="searchForm.studentId" placeholder="Enter student ID" clearable></el-input>
        </el-form-item>
        <el-form-item label="Name" >
          <el-input v-model="searchForm.name" placeholder="Enter name" clearable></el-input>
        </el-form-item>
        <el-form-item label="Class">
          <el-input v-model="searchForm.class" placeholder="Enter class" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="resetSearch">Reset</el-button>
        </el-form-item>
      </el-form>
      
      <!-- Data Table -->
      <el-table v-loading="loading" :data="studentList" border style="width: 100%">
        <el-table-column prop="studentId" label="Student ID" width="120" align="center"></el-table-column>
        <el-table-column prop="name" label="Name" width="130" align="center"></el-table-column>
        <el-table-column prop="gender" label="Gender" width="80" align="center"></el-table-column>
        <el-table-column prop="class" label="Class" width="120" align="center"></el-table-column>
        <el-table-column prop="enrollmentYear" label="Enroll Year" width="120" align="center"></el-table-column>
        <el-table-column label="Actions" align="center">
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
  name: 'StudentList',
  data() {
    return {
      loading: false,
      searchForm: {
        studentId: '',
        name: '',
        class: ''
      },
      studentList: [],
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
    ...mapActions('student', ['getStudents', 'deleteStudent']),
    
    fetchData() {
      this.loading = true
      const params = {
        page: this.pagination.page,
        size: this.pagination.size,
        studentId: this.searchForm.studentId || undefined,
        name: this.searchForm.name || undefined,
        class: this.searchForm.class || undefined
      }
      
      this.getStudents(params)
        .then(data => {
          this.studentList = data.records || []
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
        studentId: '',
        name: '',
        class: ''
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
      this.$router.push('/student/create')
    },
    
    handleEdit(row) {
      this.$router.push(`/student/edit/${row.studentId}`)
    },
    
    handleDetail(row) {
      this.$router.push(`/student/detail/${row.studentId}`)
    },
    
    handleDelete(row) {
      this.$confirm('Are you sure you want to delete this student?', 'Confirmation', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.deleteStudent(row.studentId).then(() => {
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