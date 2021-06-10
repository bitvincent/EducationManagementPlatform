<template>
  <div class="app-container">
    <h2>提交作业</h2>
    <div style="background: white;padding: 30px">
      <el-form style=" width: 400px;margin: 0 auto">
        <el-form-item label="课程名称">
          {{ className }}
        </el-form-item>
        <el-form-item label="作业次数">
          {{ homework.homeworknumber }}
        </el-form-item>
        <el-form-item label="作业题目">
          {{ homework.homeworkname }}
        </el-form-item>
        <el-form-item label="截止时间">
          {{ homework.ddl }}
        </el-form-item>
        <el-form-item label="作业详情">
          <div
            style="width: 500px"
            class="line-break"
          >
            {{ homework.description }}</div>
        </el-form-item>
        <el-form-item label="上传作业">
          <div style="margin-left: 40px">
            <el-upload
              drag
              action=""
              :show-file-list="true"
              :auto-upload="false"
              :on-change="handleUploadChange"
              accept="*"
            >
              <div style="margin-top: 55px">
                <img src="../../../assets/other_images/shangchuaun.png" alt="upload">
                <div class="el-upload__text">点击上传</div>
              </div>
              <div slot="tip" class="el-upload__tip" style="margin-top: -15px; margin-bottom: -10px" />
            </el-upload>
          </div>
        </el-form-item>
        <div style="width: 200px ;margin: 20px auto 0 auto">
          <el-button type="primary" style="width: 200px" @click="submit">提交作业</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { getClassInfo } from '@/api/classes';
import { uploadHomework } from '@/api/homework';

export default {
  name: 'Submit',
  props: {
    id: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      className: name,
      file: undefined
    }
  },
  computed: {
    homework() {
      return this.$store.getters.currentHomework
    }
  },
  created() {
    getClassInfo(this.id).then(res => {
      this.className = res.data.name
    })
  },
  methods: {
    handleUploadChange(file, fileList) {
      fileList.splice(0, fileList.length)
      fileList.push(file)
      this.file = file.raw
    },
    submit() {
      const formData = new FormData()
      formData.append('classid', this.id)
      formData.append('homeworknumber', this.homework.homeworknumber);
      formData.append('file', this.file)
      uploadHomework(formData).then(res => {
        console.log(res)
        this.$message.success('上传成功')
        this.$router.go(-1)
      })
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
