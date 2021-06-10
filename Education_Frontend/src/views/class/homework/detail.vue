<template>
  <div class="app-container">
    <div>
      <h2>{{ correct?'批改作业':'查看详情' }}</h2>
      <div style="background: white;padding: 30px">
        <div style="margin-left: 200px">
          <el-form style=" width: 400px">
            <el-form-item label="学生姓名">
              {{ homework.studentname }}
            </el-form-item>
            <el-form-item label="提交时间">
              {{ parseTime(new Date(homework.uploadtime)) }}
            </el-form-item>
            <el-form-item label="作业题目">
              {{ homework.homeworkname }}
            </el-form-item>
            <el-form-item label="作业次数">
              {{ homework.homeworknumber }}
            </el-form-item>
            <el-form-item label="作业详情">
              {{ homework.description }}
            </el-form-item>
          </el-form>

        </div>
        <HR style="color: rgb(155,155,155);width: 800px;margin-top: 80px" />

        <div style="width: 700px;margin-left: 200px; margin-top: 30px;display: flex;justify-content: space-between">
          <span>作业附件:{{ homework.docname }}</span>
          <span style="color: #666666;font-size: 14px">{{ homework.grade?homework.grade+'分':'未批阅' }}</span>
          <el-button type="primary" size="mini" @click="downloadHw">下载</el-button>
        </div>
        <div style="width:400px;margin: 50px auto">
          <el-form v-if="correct" style="width: 400px;margin-bottom: 60px">
            <el-form-item label="所得分数:  ">
              <el-input v-model="grade" style="width: 120px" type="number" />
            </el-form-item>
          </el-form>
          <div style="margin-left: 90px">
            <el-button v-if="correct" type="primary" style="width: 120px" @click="submitCorrect">提交</el-button>
            <el-button v-else type="primary" style="width: 120px;margin-top: 40px" @click="$router.go(-1)">返回</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { markHomework } from '@/api/homework';
import { parseTime } from '@/utils';
import { download } from '@/utils/ipfs'
export default {
  name: 'Detail',
  props: {
    correct: {
      type: Boolean,
      default: false
    },
    id: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      grade: 0
    }
  },
  computed: {
    homework() {
      return this.$store.getters.currentHomework
    }
  },
  methods: {
    submitCorrect() {
      const data = {
        classid: this.id,
        grade: this.grade,
        homeworknumber: this.homework.homeworknumber,
        docaddress: this.homework.docaddress
      }
      markHomework(data).then(res => {
        this.$message.success('提交成功')
        this.$router.go(-1)
      })
    },
    downloadHw() {
      download(this.homework.docaddress, this.homework.docname)
    },
    parseTime
  }
}
</script>

<style scoped>

</style>
