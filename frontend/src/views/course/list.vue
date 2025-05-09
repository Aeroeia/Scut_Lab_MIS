<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>课程列表</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleCreate">添加课程</el-button>
      </div>
      
      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="课程编号">
          <el-input v-model="searchForm.courseId" placeholder="请输入课程编号" clearable></el-input>
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="searchForm.courseName" placeholder="请输入课程名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="授课教师">
          <el-input v-model="searchForm.teacherName" placeholder="请输入教师姓名" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="courseList" border style="width: 100%">
        <el-table-column prop="courseId" label="课程编号" width="120" align="center"></el-table-column>
        <el-table-column prop="courseName" label="课程名称" min-width="150" align="center"></el-table-column>
        <el-table-column prop="teacherId" label="教师编号" width="120" align="center"></el-table-column>
        <el-table-column prop="teacherName" label="教师姓名" width="120" align="center"></el-table-column>
        <el-table-column prop="credit" label="学分" width="80" align="center"></el-table-column>
        <el-table-column prop="hours" label="学时" width="80" align="center"></el-table-column>
        <el-table-column prop="semester" label="开课学期" width="120" align="center"></el-table-column>
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
      this.$router.push(`/course/edit/${row.courseId}`)
    },
    
    handleDetail(row) {
      this.$router.push(`/course/detail/${row.courseId}`)
    },
    
    handleDelete(row) {
      this.$confirm('确认删除该课程吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteCourse(row.courseId).then(() => {
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