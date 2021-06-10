<template>
  <div class="app-container">
    <h3 class="header">发布课程</h3>
    <div class="form-container">
      <el-form ref="form" :model="form" label-width="90px" class="form" :rules="rules" label-position="left">
        <el-form-item label="课程名称" prop="classname">
          <el-input v-model="form.classname" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="课程门类" prop="category">
          <el-select v-model="form.category" placeholder="请选择课程门类" style="width: 100%">
            <el-option
              v-for="item in $store.state.homepage.classCategories"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开课时间" prop="starttime">
          <el-date-picker
            v-model="form.starttime"
            type="date"
            placeholder="请选择开课日期"
            :picker-options="startPickerOptions"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结课时间" prop="endtime">
          <el-date-picker
            v-model="form.endtime"
            type="date"
            placeholder="请选择结课日期"
            :picker-options="endPickerOptions"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="学时安排" prop="learnhours">
          <el-input-number
            v-model="form.learnhours"
            placeholder="请输入每周学时（1～20）"
            :min="1"
            :max="20"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="课程简介" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :autosize="{minRows: 4, maxRows: 4}"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="课程图片" prop="file">
          <el-upload
            class="upload"
            drag
            action=""
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleUploadChange"
            accept="image/*"
          >
            <img
              v-if="form.file"
              :src="fileUrl"
              alt="image"
              class="image"
            >
            <div v-else style="margin-top: 25px">
              <img src="../../assets/other_images/shangchuaun.png" alt="upload">
              <div class="el-upload__text">点击上传240X135px的图片</div>
            </div>
            <div slot="tip" class="el-upload__tip" style="margin-top: -15px; margin-bottom: -10px">
              只能上传不超过 2MB 的image/*文件
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button class="button" type="primary" @click="$router.go(-1)">取消</el-button>
          <el-button :loading="submitLoading" style="margin-left: 120px" class="button" type="primary" @click="submit">
            {{ submitLoading ? '提交中' : '发布课程' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { publishClass } from '@/api/classes'

export default {
  name: 'PublishClass',
  data() {
    return {
      form: {
        classname: '',
        category: undefined,
        starttime: '',
        endtime: '',
        learnhours: undefined,
        description: '',
        file: undefined
      },
      rules: {
        classname: [
          { required: true, message: '请输入课程名称', trigger: 'change' },
          { min: 2, max: 40, message: '课程名称长度在 2 到 40 个字符之间', trigger: 'blur' }
        ],
        category: [{ required: true, message: '请选择课程门类', trigger: 'change' }],
        starttime: [
          { required: true, message: '请选择开课时间', trigger: 'change' },
          { validator:
              (_, value, cb) => {
                if (this.form.endtime &&
                  new Date(new Date(this.form.endtime).setHours(23, 59, 59, 999)).getTime() <=
                  new Date(value).getTime()) {
                  return cb(new Error('开课时间不得晚于结课时间'))
                }
                return cb()
              },
          trigger: 'blur'
          }
        ],
        endtime: [
          { required: true, message: '请选择结课时间', trigger: 'change' },
          { validator:
              (_, value, cb) => {
                if (this.form.starttime &&
                  new Date(this.form.starttime).getTime() >=
                  new Date(new Date(value).setHours(23, 59, 59, 999)).getTime()) {
                  return cb(new Error('结课时间不得早于开课时间'))
                }
                return cb()
              },
          trigger: 'blur'
          }
        ],
        learnhours: [
          { required: true, message: '请输入每周学时', trigger: 'blur' }
        ],
        description: [{ required: true, message: '请输入课程简介', trigger: 'change' }],
        file: [{ required: true, message: '请上传课程图片', trigger: 'change' }]
      },
      fileUrl: '',
      startPickerOptions: {
        disabledDate:
          time => time.getTime() < new Date(new Date().setHours(0, 0, 0, 0)).getTime() ||
            this.form.endtime &&
            time.getTime() >=
            new Date(new Date(this.form.endtime).setHours(23, 59, 59, 999)).getTime()
      },
      endPickerOptions: {
        disabledDate:
          time => time.getTime() < new Date(new Date().setHours(0, 0, 0, 0)).getTime() ||
            this.form.starttime &&
            new Date(time.setHours(23, 59, 59, 999)).getTime() <=
            new Date(this.form.starttime).getTime()
      },
      submitLoading: false
    }
  },
  methods: {
    handleUploadChange(file) {
      if (file.size / 1024 / 1024 > 2) {
        this.$notify.error({
          title: '文件大小超出上限',
          message: '课程图片大小超过 2MB'
        })
        return
      }
      // To avoid a bug of upload rule: error won't disappear even if coverUrl is changed.
      this.$nextTick(() => {
        this.$refs.form.clearValidate('file')
      })

      // get dataUrl to show preview
      const reader = new FileReader()
      reader.onload = () => {
        if (reader.result) {
          this.fileUrl = reader.result.toString()
        }
      }
      reader.readAsDataURL(file.raw)
      this.form.file = file.raw
    },
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          const formData = new FormData()
          for (const k in this.form) {
            // eslint-disable-next-line no-prototype-builtins
            if (this.form.hasOwnProperty(k)) formData.append(k, this.form[k])
          }
          publishClass(formData)
            .then(res => {
              this.$notify.success({
                title: '成功',
                message: '成功发布课程',
                duration: 5 * 1000
              })
              const id = res.data.classid
              this.$router.push(id ? { name: 'ClassDetail', params: { id }} : { name: 'MyClassList' })
            })
            .catch(err => { console.log(err) })
            .finally(() => { this.submitLoading = false })
        }
      })
    }
  }
}
</script>

<style scoped>
.header {
  font-size: 22px;
  font-weight: 500;
}

.form-container {
  padding: 40px;
  width: 100%;
  background-color: #fff;
  margin-bottom: 20px;
}

.form {
  width: 600px;
  margin: 0 auto;
}

.upload ::v-deep.el-upload-dragger {
  width: 242px;
  height: 137px;
}

.image {
  width: 240px;
  height: 135px;
  object-fit: scale-down;
}

.button {
  width: 100px;
  background-color: #10429a;
  border-color: #10429a;
}
</style>
