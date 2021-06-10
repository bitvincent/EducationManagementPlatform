<template>
  <div class="header-wrapper">
    <img
      src="@/assets/logo_default.png"
      alt="logo"
      title="首页"
      style="cursor: pointer"
      @click="$router.push({name: 'Homepage'})"
    >
    <div v-if="loggedIn" style="float: right">
      <span class="name">{{ name }}</span>
      <el-button type="text" @click="accountSetting">账号设置</el-button>
      <el-button type="text" @click="doLogout">退出</el-button>
    </div>
    <el-dialog
      title="账号设置"
      :visible.sync="dialogVisible"
      width="30%"
      center
      :show-close="false"
      :before-close="beforeClose"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="原密码" prop="old">
          <el-input v-model="form.old" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="修改密码" prop="newPassword">
          <el-input v-model="form.newPassword" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="确认密码" prop="password2">
          <el-input v-model="form.password2" type="password" autocomplete="off" />
        </el-form-item>
        <el-button style="margin-left: 20px" @click="dialogVisible = false">取 消</el-button>
        <el-button style="float:right" type="primary" :loading="loading" @click="handleConfirm">确 定</el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { settingMplx } from '@/api/user'
export default {
  data() {
    const validateRePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.form.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    }
    return {
      dialogVisible: false,
      loading: false,
      form: {
        old: '',
        newPassword: '',
        password2: ''
      },
      rules: {
        old: [{ required: true, trigger: 'blur', message: '旧密码必填' }],
        newPassword: [{ required: true, trigger: 'blur', message: '新密码必填' }],
        password2: [{ trigger: 'blur', validator: validateRePassword }]
      }
    }
  },
  computed: {
    loggedIn() {
      return this.$store.getters.loggedIn
    },
    isTeacher() {
      return this.$store.getters.isTeacher
    },
    name() {
      if (!this.loggedIn) return ''
      return this.isTeacher ? this.$store.state.user.info.teachername : this.$store.state.user.info.studentname
    }
  },
  methods: {
    doLogout() {
      this.$store.dispatch('user/logout').then(() => {
        this.$router.push('/login')
      })
    },
    accountSetting() {
      this.dialogVisible = true
    },
    beforeClose() {
      this.$refs.form.resetFields()
    },
    handleConfirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          const data = { oldpassword: this.form.old, newpassword: this.form.newPassword }
          settingMplx(this.$store.state.user.userType, data).then((res) => {
            if (res.status === 0) {
              // ok
              this.$message({
                type: 'success',
                message: '修改密码成功'
              })
              this.dialogVisible = false
            }
            this.loading = false
          }).catch((e) => {
            this.loading = false
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.header-wrapper {
  display: flex;
  width: 1200px;
  height: 80px;
  justify-content: space-between;
  align-items: center;
}

.name {
  margin-right: 10px;
}
</style>
