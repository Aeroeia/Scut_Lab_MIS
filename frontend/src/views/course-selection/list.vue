<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>选课列表</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleCreate" v-if="isAdmin">添加选课</el-button>
      </div>
      
      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="学号">
          <el-input v-model="searchForm.studentId" placeholder="请输入学号" clearable></el-input>
        </el-form-item>
        <el-form-item label="学生姓名">
          <el-input v-model="searchForm.studentName" placeholder="请输入学生姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="课程编号">
          <el-input v-model="searchForm.courseId" placeholder="请输入课程编号" clearable></el-input>
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="searchForm.courseName" placeholder="请输入课程名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="选课年份">
          <el-date-picker
            v-model="searchForm.selectionYear"
            type="year"
            value-format="yyyy"
            placeholder="选择年份">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="selectionList" border style="width: 100%">
        <el-table-column prop="id" label="选课ID" width="80" align="center"></el-table-column>
        <el-table-column prop="studentId" label="学号" width="120" align="center"></el-table-column>
        <el-table-column prop="studentName" label="学生姓名" width="120" align="center"></el-table-column>
        <el-table-column prop="courseId" label="课程编号" width="120" align="center"></el-table-column>
        <el-table-column prop="courseName" label="课程名称" min-width="150" align="center"></el-table-column>
        <el-table-column prop="teacherName" label="授课教师" width="120" align="center"></el-table-column>
        <el-table-column prop="selectionYear" label="选课年份" width="100" align="center"></el-table-column>
        <el-table-column prop="score" label="成绩" width="80" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.score !== null ? scope.row.score : '未录入' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template slot-scope="scope">
            <el-button 
              type="text" 
              size="small" 
              v-if="isTeacher && scope.row.score === null"
              @click="handleInputScore(scope.row)">
              录入成绩
            </el-button>
            <el-button 
              type="text" 
              size="small" 
              class="danger-text" 
              v-if="isAdmin"
              @click="handleDelete(scope.row)">
              删除
            </el-button>
            <el-button 
              type="text" 
              size="small" 
              @click="handleViewStudent(scope.row)">
              学生详情
            </el-button>
            <el-button 
              type="text" 
              size="small" 
              @click="handleViewCourse(scope.row)">
              课程详情
            </el-button>
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
    ...mapGetters(['userRole']),
    isAdmin() {
      return this.userRole === '1' // 1代表管理员角色
    },
    isTeacher() {
      return this.userRole === '2' // 2代表教师角色
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
    ...mapActions('course-selection', ['getCourseSelections', 'deleteCourseSelection']),
    
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
    
    handleViewStudent(row) {
      this.$router.push(`/student/detail/${row.studentId}`)
    },
    
    handleViewCourse(row) {
      this.$router.push(`/course/detail/${row.courseId}`)
    },
    
    handleDelete(row) {
      this.$confirm('确认删除该选课记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteCourseSelection(row.id).then(() => {
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