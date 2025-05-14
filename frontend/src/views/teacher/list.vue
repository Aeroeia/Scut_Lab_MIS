<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>Teacher List</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleCreate">Add Teacher</el-button>
      </div>
      
      <!-- Search Area -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="ID">
          <el-input v-model="searchForm.teacherId" placeholder="Enter ID" clearable></el-input>
        </el-form-item>
        <el-form-item label="Name">
          <el-input v-model="searchForm.name" placeholder="Enter name" clearable></el-input>
        </el-form-item>
        <el-form-item label="Title">
          <el-select v-model="searchForm.title" placeholder="Select title" clearable>
            <el-option label="Lecturer" value="Lecturer"></el-option>
            <el-option label="Associate Professor" value="Associate"></el-option>
            <el-option label="Professor" value="Professor"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="resetSearch">Reset</el-button>
        </el-form-item>
      </el-form>
      
      <!-- Data Table -->
      <el-table v-loading="loading" :data="teacherList" border style="width: 100%">
        <el-table-column prop="teacherId" label="ID" width="120" align="center"></el-table-column>
        <el-table-column prop="name" label="Name" width="120" align="center"></el-table-column>
        <el-table-column prop="gender" label="Gender" width="80" align="center"></el-table-column>
        <el-table-column prop="title" label="Title" width="120" align="center"></el-table-column>
        <el-table-column prop="department" label="Department" width="150" align="center"></el-table-column>
        <el-table-column prop="email" label="Email" min-width="180" align="center"></el-table-column>
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
  name: 'TeacherList',
  data() {
    return {
      loading: false,
      searchForm: {
        teacherId: '',
        name: '',
        title: ''
      },
      teacherList: [],
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
    ...mapActions('teacher', ['getTeachers', 'deleteTeacher']),
    
    fetchData() {
      this.loading = true
      const params = {
        page: this.pagination.page,
        size: this.pagination.size,
        teacherId: this.searchForm.teacherId || undefined,
        name: this.searchForm.name || undefined,
        title: this.searchForm.title || undefined
      }
      
      this.getTeachers(params)
        .then(data => {
          this.teacherList = data.records || []
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
        teacherId: '',
        name: '',
        title: ''
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
      this.$router.push('/teacher/create')
    },
    
    handleEdit(row) {
      this.$router.push(`/teacher/edit/${row.teacherId}`)
    },
    
    handleDetail(row) {
      this.$router.push(`/teacher/detail/${row.teacherId}`)
    },
    
    handleDelete(row) {
      this.$confirm('Are you sure you want to delete this teacher?', 'Confirm', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.deleteTeacher(row.teacherId).then(() => {
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