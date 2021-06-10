<template>
  <div class="ctn">
    <div class="text-center"><img src="@/assets/touxiang.png"></div>
    <div v-if="login">
      <p class="text-center name">{{ name }}</p>
      <div v-if="isTeacher">
        <p class="text-center" style="margin-top:10px">
          <el-button type="text" @click="myClass">我发布的课程</el-button>
        </p>
      </div>
      <div v-else>
        <p class="text-center" style="margin-top:10px">
          <el-button type="text" @click="myClass">我的课程</el-button>
        </p>
        <p class="text-center"><el-button type="text" @click="toMyCertificate">我的证书</el-button></p>
      </div>
    </div>
    <div v-else>
      <el-button type="primary" class="loginBtn" @click="onLogin">登录/注册</el-button>
    </div>
  </div>
</template>

<script>
export default {
  computed: {
    login() {
      return !!this.$store.getters.loggedIn
    },

    isTeacher() {
      return this.$store.getters.isTeacher
    },
    name() {
      return this.isTeacher ? this.$store.state.user.info.teachername : this.$store.state.user.info.studentname
    }
  },

  methods: {
    onLogin() {
      this.$router.push('/login')
    },
    myClass() {
      this.$router.push({ name: 'MyClassList' })
    },
    toMyCertificate() {
      this.$router.push('/my-certificate')
    }
  }
}
</script>

<style lang="scss" scoped>
.text-center {
    text-align: center;
    margin: 0 0;
}

.ctn {
  height: 300px;
  padding: 20px;
  background-color: white;
  border-radius: 10px;
}

.name {
  font-size: 18px;
  margin-top: 10px;
}

.loginBtn {
  width:100%;
  border-radius:20px;
  margin-top: 60px;
}
</style>

