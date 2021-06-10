<template>
  <div>
    <h2>课程作业</h2>
    <div style="background: white;padding: 30px 30px 200px 30px">
      <el-tabs v-model="activeName" @tab-click="getData">
        <el-tab-pane label="发布作业" name="publish">
          <el-table :header-cell-style="{'background-color':'#5893f9','color':'white'}" :data="publishTable">
            <el-table-column align="center" label="作业次数" prop="homeworknumber" />
            <el-table-column align="center" label="作业题目" prop="homeworkname" />
            <el-table-column align="center" label="发布时间">
              <template slot-scope="{row}">
                {{ parseTime(new Date(row.starttime)) }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="截止时间">
              <template slot-scope="{row}">
                {{ parseTime(new Date(row.ddl)) }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="查看详情">
              <template slot-scope="{row}">
                <el-button size="mini" type="primary" @click="handlePublishDetail(row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin: 100px auto 0 auto; width: 200px">
            <el-button type="primary" @click="handlePublish">添加作业</el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane label="未批改作业" name="correct">
          <el-table :header-cell-style="{'background-color':'#5893f9','color':'white'}" :data="correctTable">
            <el-table-column align="center" label="作业次数" prop="homeworknumber" />
            <el-table-column align="center" label="作业题目" prop="homeworkname" />
            <el-table-column align="center" label="学生" prop="studentname" />
            <el-table-column align="center" label="上传时间">
              <template slot-scope="{row}">
                {{ parseTime(new Date(row.uploadtime)) }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="截止时间">
              <template slot-scope="{row}">
                {{ parseTime(new Date(row.ddl)) }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="查看详情">
              <template slot-scope="{row}">
                <el-button size="mini" type="primary" @click="handleCorrect(row)">批改</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="已批改作业" name="corrected">
          <el-table :header-cell-style="{'background-color':'#5893f9','color':'white'}" :data="correctedTable">
            <el-table-column align="center" label="作业次数" prop="homeworknumber" />
            <el-table-column align="center" label="作业题目" prop="homeworkname" />
            <el-table-column align="center" label="学生" prop="studentname" />
            <el-table-column align="center" label="上传时间">
              <template slot-scope="{row}">
                {{ parseTime(new Date(row.uploadtime)) }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="批改时间">
              <template slot-scope="{row}">
                {{ parseTime(new Date(row.marktime)) }}
              </template>
            </el-table-column>
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
import { getMarkedHomeworkTeacher, getPublishedHomework, getUnMarkedHomeworkTeacher } from '@/api/homework';
import { parseTime } from '@/utils';

export default {
  name: 'Teacher',
  props: {
    id: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      activeName: 'publish',
      /**
       * homeworknumber(作业次数，int)
       * homeworkname(作业名称)
       * description(作业详情)
       * starttime(发布时间 YYYY-MM-DD HH:MM:SS)
       * ddl(截止时间 YYYY-MM-DD HH:MM:SS)
       */
      publishTable: [{ homeworknumber: 1, homeworkname: 'test', description: 'description', starttime: '1234', ddl: 567 }],
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
      correctTable: [{ homeworknumber: 1, homeworkname: 'test', studentname: 'test', description: 'description', uploadtime: '1234', ddl: 567, docname: 'doc', docaddress: 'addr' }],
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
      if (this.activeName === 'publish') {
        this.getPublish()
      } else if (this.activeName === 'correct') {
        this.getCorrect()
      } else {
        this.getCorrected()
      }
    },
    async getPublish() {
      const res = await getPublishedHomework(this.id)
      this.publishTable = res.data
    },
    async getCorrect() {
      const res = await getUnMarkedHomeworkTeacher(this.id)
      this.correctTable = res.data
    },
    async getCorrected() {
      const res = await getMarkedHomeworkTeacher(this.id)
      this.correctedTable = res.data
    },
    handlePublish() {
      this.$router.push({ name: 'HomeworkPublish', params: { id: this.id, homeworknumber: this.publishTable.length + 1 }})
    },
    handlePublishDetail(homework) {
      this.$router.push({ name: 'HomeworkPublishDetail', params: { id: this.id, hwNum: homework.homeworknumber }})
    },
    handleCorrect(homework) {
      this.$store.commit('homework/SET_HOMEWORK', homework)
      this.$router.push({ name: 'HomeworkCorrect', params: { id: this.id }})
    },
    handleCorrected(homework) {
      this.$store.commit('homework/SET_HOMEWORK', homework)
      this.$router.push({ name: 'HomeworkCorrected', params: { id: this.id }})
    },
    parseTime
  }
}
</script>

<style scoped>

</style>
