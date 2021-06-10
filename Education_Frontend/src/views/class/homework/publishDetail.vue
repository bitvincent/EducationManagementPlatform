<template>
  <div class="app-container">
    <h2>查看详情</h2>
    <div style="background: white;padding: 30px">
      <el-form style=" width: 400px;margin: 0 auto">
        <el-form-item label="课程名称">
          {{ className }}
        </el-form-item>
        <el-form-item label="课程门类">
          {{ category }}
        </el-form-item>
        <el-form-item label="作业次数">
          {{ hwNum }}
        </el-form-item>
        <el-form-item label="作业题目">
          {{ hwName }}
        </el-form-item>
        <el-form-item label="截止时间">
          {{ ddl }}
        </el-form-item>
        <el-form-item label="作业详情">
          <div
            style="width: 500px"
            class="line-break"
          >
            {{ description }}</div>
        </el-form-item>
        <div style="width: 200px ;margin: 50px auto 0 auto">
          <el-button type="primary" style="width: 200px" @click="$router.go(-1)"> 返  回</el-button>

        </div>

      </el-form>
    </div>
  </div>
</template>

<script>
import { getTeacherHomeworkInfo } from '@/api/homework';
import { parseTime } from '@/utils';

export default {
  name: 'PublishDetail',
  props: {
    id: {
      type: String,
      default: ''
    },
    hwNum: {
      type: Number,
      default: -1
    }
  },
  data() {
    return {
      className: '',
      category: '',
      hwName: '',
      ddl: '',
      description: ''
    }
  },
  created() {
    getTeacherHomeworkInfo({ classid: this.id, homeworknumber: this.hwNum }).then(res => {
      const data = res.data
      this.className = data.classname
      this.category = this.getClassCategory(data.category).label
      this.hwName = data.homeworkname
      this.description = data.description
      this.ddl = parseTime(new Date(data.ddl))
    })
  },
  methods: {
    getClassCategory(number) {
      return this.$store.getters.classCategories.find(v => v.id === number)
    }
  }
}
</script>

<style scoped>
.line-break{
  white-space:normal;
  word-break:break-all;
  word-wrap:break-word;
}
</style>
