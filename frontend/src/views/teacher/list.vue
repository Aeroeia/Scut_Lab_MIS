<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>教师列表</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleCreate">添加教师</el-button>
      </div>
      
      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="工号">
          <el-input v-model="searchForm.teacherId" placeholder="请输入工号" clearable></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="职称">
          <el-select v-model="searchForm.title" placeholder="请选择职称" clearable>
            <el-option label="讲师" value="讲师"></el-option>
            <el-option label="副教授" value="副教授"></el-option>
            <el-option label="教授" value="教授"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="teacherList" border style="width: 100%">
        <el-table-column prop="teacherId" label="工号" width="120" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" width="120" align="center"></el-table-column>
        <el-table-column prop="gender" label="性别" width="80" align="center"></el-table-column>
        <el-table-column prop="title" label="职称" width="120" align="center"></el-table-column>
        <el-table-column prop="department" label="所属院系" width="150" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="180" align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleDetail(scope.row)">详情</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" class="danger-text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页组件 -->
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
      this.$confirm('确认删除该教师吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteTeacher(row.teacherId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {
        this.$message.info('已取消删除')
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