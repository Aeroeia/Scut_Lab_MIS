<template>
  <div class="container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <span>课程详情</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回列表</el-button>
      </div>
      
      <el-row :gutter="20" v-if="!loading">
        <!-- 基本信息 -->
        <el-col :span="8">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>基本信息</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="handleEdit">编辑</el-button>
            </div>
            <div class="info-item">
              <span class="label">课程编号：</span>
              <span class="content">{{ courseInfo.courseId }}</span>
            </div>
            <div class="info-item">
              <span class="label">课程名称：</span>
              <span class="content">{{ courseInfo.courseName }}</span>
            </div>
            <div class="info-item">
              <span class="label">授课教师：</span>
              <span class="content">{{ courseInfo.teacherName }} ({{ courseInfo.teacherId }})</span>
            </div>
            <div class="info-item">
              <span class="label">学分：</span>
              <span class="content">{{ courseInfo.credit }}</span>
            </div>
            <div class="info-item">
              <span class="label">学时：</span>
              <span class="content">{{ courseInfo.hours }}</span>
            </div>
            <div class="info-item">
              <span class="label">开课学期：</span>
              <span class="content">{{ courseInfo.semester }}</span>
            </div>
            <div class="info-item">
              <span class="label">课程描述：</span>
              <span class="content description">{{ courseInfo.description || '暂无描述' }}</span>
            </div>
          </el-card>
        </el-col>
        
        <!-- 选课学生 -->
        <el-col :span="16">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>选课学生</span>
              <span class="student-count">共 {{ studentCount }} 人</span>
            </div>
            
            <!-- 学生查询 -->
            <el-form :inline="true" :model="searchForm" class="demo-form-inline mb-20">
              <el-form-item label="学号">
                <el-input v-model="searchForm.studentId" placeholder="请输入学号" clearable size="small"></el-input>
              </el-form-item>
              <el-form-item label="姓名">
                <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable size="small"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" size="small" @click="searchStudents">查询</el-button>
                <el-button size="small" @click="resetSearch">重置</el-button>
              </el-form-item>
            </el-form>
            
            <!-- 学生列表 -->
            <el-table
              v-if="students.length > 0"
              :data="students"
              border
              style="width: 100%">
              <el-table-column prop="studentId" label="学号" width="120" align="center"></el-table-column>
              <el-table-column prop="name" label="姓名" width="120" align="center"></el-table-column>
              <el-table-column prop="gender" label="性别" width="80" align="center"></el-table-column>
              <el-table-column prop="class" label="班级" width="120" align="center"></el-table-column>
              <el-table-column prop="score" label="成绩" width="100" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.score !== null ? scope.row.score : '未录入' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="viewStudentDetail(scope.row)">查看详情</el-button>
                  <el-button v-if="isTeacher" type="text" size="small" @click="editScore(scope.row)">录入成绩</el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <el-empty v-else description="暂无选课学生"></el-empty>
            
            <!-- 分页 -->
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
      return this.userRole === '2' // 2代表教师角色
    }
  },
  created() {
    this.courseId = this.$route.params.id
    if (!this.courseId) {
      this.$message.error('课程ID不能为空')
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
      
      // 获取课程信息
      const coursePromise = this.getCourseDetail(this.courseId)
        .then(data => {
          this.courseInfo = data
        })
      
      // 获取选课学生
      const studentsPromise = this.fetchStudents()
      
      // 等待所有请求完成
      Promise.all([coursePromise, studentsPromise])
        .catch(() => {
          this.$message.error('获取课程信息失败')
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