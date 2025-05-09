<template>
  <div class="container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>成绩列表</span>
      </div>
      
      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
        <el-form-item label="学号">
          <el-input v-model="searchForm.studentId" placeholder="请输入学号" clearable></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="searchForm.studentName" placeholder="请输入姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="课程编号">
          <el-input v-model="searchForm.courseId" placeholder="请输入课程编号" clearable></el-input>
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="searchForm.courseName" placeholder="请输入课程名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="成绩状态">
          <el-select v-model="searchForm.scoreStatus" placeholder="请选择" clearable>
            <el-option label="已录入" value="1"></el-option>
            <el-option label="未录入" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="scoreList" border style="width: 100%">
        <el-table-column prop="id" label="选课ID" width="80" align="center"></el-table-column>
        <el-table-column prop="studentId" label="学号" width="120" align="center"></el-table-column>
        <el-table-column prop="studentName" label="学生姓名" width="120" align="center"></el-table-column>
        <el-table-column prop="courseId" label="课程编号" width="120" align="center"></el-table-column>
        <el-table-column prop="courseName" label="课程名称" min-width="150" align="center"></el-table-column>
        <el-table-column prop="credit" label="学分" width="80" align="center"></el-table-column>
        <el-table-column prop="score" label="成绩" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.score !== null" :type="getScoreTagType(scope.row.score)">
              {{ scope.row.score }}
            </el-tag>
            <el-tag v-else type="info">未录入</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="selectionYear" label="选课年份" width="100" align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="120">
          <template slot-scope="scope">
            <el-button 
              type="text" 
              size="small" 
              v-if="isTeacher"
              @click="handleEdit(scope.row)">
              {{ scope.row.score !== null ? '修改成绩' : '录入成绩' }}
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
  name: 'ScoreList',
  data() {
    return {
      loading: false,
      searchForm: {
        studentId: '',
        studentName: '',
        courseId: '',
        courseName: '',
        scoreStatus: ''
      },
      scoreList: [],
      pagination: {
        page: 1,
        size: 10
      },
      total: 0
    }
  },
  computed: {
    ...mapGetters(['userRole']),
    isTeacher() {
      return this.userRole === '2' // 2代表教师角色
    }
  },
  created() {
    // 如果是从其他页面跳转过来的，获取查询参数
    if (this.$route.query.courseId) {
      this.searchForm.courseId = this.$route.query.courseId
    }
    if (this.$route.query.teacherId) {
      this.searchForm.teacherId = this.$route.query.teacherId
    }
    
    this.fetchData()
  },
  methods: {
    ...mapActions('score', ['getScores']),
    
    fetchData() {
      this.loading = true
      const params = {
        page: this.pagination.page,
        size: this.pagination.size,
        studentId: this.searchForm.studentId || undefined,
        studentName: this.searchForm.studentName || undefined,
        courseId: this.searchForm.courseId || undefined,
        courseName: this.searchForm.courseName || undefined,
        teacherId: this.searchForm.teacherId || undefined,
        scoreStatus: this.searchForm.scoreStatus || undefined
      }
      
      this.getScores(params)
        .then(data => {
          this.scoreList = data.records || []
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
        scoreStatus: '',
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
    
    handleEdit(row) {
      // 跳转到成绩编辑页面
      this.$router.push({
        path: `/score/edit/${row.id}`,
        query: {
          studentId: row.studentId,
          courseId: row.courseId
        }
      })
    },
    
    // 根据成绩返回不同的Tag类型
    getScoreTagType(score) {
      if (score >= 90) return 'success'
      if (score >= 75) return 'primary'
      if (score >= 60) return ''
      return 'danger'
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
</style> 