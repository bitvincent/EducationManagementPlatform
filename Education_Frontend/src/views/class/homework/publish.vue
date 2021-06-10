<template>
  <div class="app-container">
    <h3>发布作业</h3>
    <div style="background: white; padding: 50px">
      <el-form style=" width: 400px;margin: 0 auto">
        <el-form-item label="课程名称">{{ className }}</el-form-item>
        <el-form-item label="作业次数">{{ homeworknumber }}</el-form-item>
        <el-form-item label="作业题目">
          <el-input v-model="homeworkname" style="width: 300px" placeholder="请输入作业题目" />
        </el-form-item>
        <el-form-item label="截止时间">
          <el-date-picker
            v-model="ddl"
            type="datetime"
            placeholder="选择截止时间"
          />
        </el-form-item>
        <el-form-item label="作业详情">
          <el-input v-model="description" style="width: 300px" type="textarea" />
        </el-form-item>

        <div style="margin-top: 70px">
          <el-button style="width: 150px" @click="$router.go(-1)">返回</el-button>
          <el-button type="primary" style="width:150px; margin-left: 50px" @click="publish">发布</el-button>
        </div>
      </el-form>
    </div>

  </div>

</template>
<script>
import { getClassInfo } from '@/api/classes';
import { parseTime } from '@/utils';
import { publishHomework } from '@/api/homework';

export default {
  name: 'Publish',
  props: {
    id: {
      type: String,
      default: ''
    },
    homeworknumber: {
      type: Number,
      default: -1
    }
  },
  data() {
    return {
      className: '',
      homeworkname: '',
      ddl: '',
      description: ''
    }
  },
  created() {
    console.log(this.homeworknumber)
    getClassInfo(this.id).then(res => {
      this.className = res.data.name
    })
  },
  methods: {
    publish() {
      publishHomework({
        classid: this.id,
        homeworknumber: this.homeworknumber,
        homeworkname: this.homeworkname,
        description: this.description,
        ddl: parseTime(new Date(this.ddl))
      }).then(res => {
        console.log(res)
        this.$message.success('发布成功')
        this.$router.push({ name: 'Homework', params: { id: this.id }})
      })
    }
  }

}
</script>

<style scoped>

</style>
