<template>
  <div class="app-container">
    <h2>我的作业</h2>
    <div style="background: white;padding: 30px 30px 200px 30px">
      <el-tabs v-model="activeName" @tab-click="getData">
        <el-tab-pane
          label="未提交"
          name="notSubmitted"
        >
          <el-table :header-cell-style="{'background-color':'#5893f9','color':'white'}" :data="notSubmittedTable">
            <el-table-column align="center" label="作业次数" prop="homeworknumber" />
            <el-table-column align="center" label="作业题目" prop="homeworkname" />
            <el-table-column align="center" label="发布时间" prop="starttime" />
            <el-table-column align="center" label="截止时间" prop="ddl" />
            <el-table-column align="center" label="提交作业">
              <template slot-scope="{row}">
                <el-button size="mini" type="primary" @click="handleSubmit(row)">提交</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="未批阅" name="submitted">
          <el-table :header-cell-style="{'background-color':'#5893f9','color':'white'}" :data="submittedTable">
            <el-table-column align="center" label="作业次数" prop="homeworknumber" />
            <el-table-column align="center" label="作业题目" prop="homeworkname" />
            <el-table-column align="center" label="上传时间" prop="uploadtime" />
            <el-table-column align="center" label="截止时间" prop="ddl" />
            <el-table-column align="center" label="查看详情">
              <template slot-scope="{row}">
                <el-button size="mini" type="primary" @click="handleNotCorrected(row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="已批阅" name="corrected">
          <el-table :header-cell-style="{'background-color':'#5893f9','color':'white'}" :data="correctedTable">
            <el-table-column align="center" label="作业次数" prop="homeworknumber" />
            <el-table-column align="center" label="作业题目" prop="homeworkname" />
            <el-table-column align="center" label="上传时间" prop="uploadtime" />
            <el-table-column align="center" label="批改时间" prop="marktime" />
            <el-table-column align="center" label="成绩" prop="grade" />
            <el-table-column align="center" label="查看详情">
              <template slot-scope="{row}">
                <el-button size="mini" type="primary" @click="handleCorrected(row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { getMarkedHomeworkStudent, getUnMarkedHomeworkStudent, getUnUploadedHomework } from '@/api/homework';

export default {
  name: 'Student',
  props: {
    id: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      activeName: 'notSubmitted',
      /**
       * homeworknumber(作业次数，int)
       * homeworkname(作业名称)
       * description(作业详情)
       * starttime(发布时间 YYYY-MM-DD HH:MM:SS)
       * ddl(截止时间 YYYY-MM-DD HH:MM:SS)
       */
      notSubmittedTable: [],
      /**
       * homeworknumber(作业次数，int)
       * homeworkname(作业题目)
       * studentname(学生姓名)
       * uploadtime(作业上传时间 YYYY-MM-DD HH:MM:SS)
       * ddl(截止时间 YYYY-MM-DD HH:MM:SS)
       * description(作业详情，批改作业页面需要)
       * docname(学生作业的文档名称，批改作业页面需要)
       * docaddress(学生作业的文档下载地址，批改作业页面需要)
       */
      submittedTable: [],
      /**
       *  homeworknumber(作业次数，int)
       *  homeworkname(作业题目)
       *  studentname(学生姓名)
       *  uploadtime(作业上传时间 YYYY-MM-DD HH:MM:SS)
       *  marktime(作业批改时间 YYYY-MM-DD HH:MM:SS)
       *  description(作业详情，批改作业页面需要)
       *  docname(学生作业的文档名称，批改作业页面需要)
       *  docaddress(学生作业的文档下载地址，批改作业页面需要)
       *  grade(分数)
       */
      correctedTable: [{ homeworknumber: 1, homeworkname: 'test', studentname: 'test', description: 'description', uploadtime: '1234', marktime: 'marktime', ddl: 567, docname: 'doc', docaddress: 'addr', grade: 'grade' }]
    }
  },
  created() {
    this.getData()
  },
  methods: {
    getData() {
      if (this.activeName === 'notSubmitted') {
        this.getNotSubmitted()
      } else if (this.activeName === 'submitted') {
        this.getSubmitted()
      } else {
        this.getCorrected()
      }
    },
    async getNotSubmitted() {
      const res = await getUnUploadedHomework(this.id)
      this.notSubmittedTable = res.data
    },
    async getSubmitted() {
      const res = await getUnMarkedHomeworkStudent(this.id)
      this.submittedTable = res.data
    },
    async getCorrected() {
      const res = await getMarkedHomeworkStudent(this.id)
      this.correctedTable = res.data
    },
    handleSubmit(homework) {
      this.$store.commit('homework/SET_HOMEWORK', homework)
      this.$router.push({ name: 'HomeworkSubmit', params: { id: this.id, hwNum: homework.homeworknumber }})
    },
    handleNotCorrected(homework) {
      const studentname = this.$store.state.user.info.studentname
      console.log(this.$store.state)
      homework.studentname = studentname
      this.$store.commit('homework/SET_HOMEWORK', homework)
      this.$router.push({ name: 'HomeworkNotMarked', params: { id: this.id }})
    },
    handleCorrected(homework) {
      this.$store.commit('homework/SET_HOMEWORK', homework)
      this.$router.push({ name: 'HomeworkMarked', params: { id: this.id }})
    }
  }
}
</script>

<style scoped>

</style>
