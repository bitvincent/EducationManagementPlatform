<template>
  <div class="bg" :style="{ backgroundImage: 'url(' + bgSrc + ')' }">
    <div class="logo"><img src="@/assets/logo.png"></div>
    <div class="front">
      <p class="header">{{ isLogin ? "账号登录" : "账号注册" }}</p>
      <div class="main">
        <el-radio-group
          v-model="loginType"
          size="mini"
          style="width: 100%; margin-bottom: 10px"
          @change="handleTypeChange"
        >
          <el-radio-button label="student">学生</el-radio-button>
          <el-radio-button label="teacher">老师</el-radio-button>
        </el-radio-group>
        <div v-if="isLogin">
          <!-- 登录表单 -->
          <el-form
            ref="loginForm"
            :rules="rules"
            :model="form"
            label-width="80px"
            inline-message
          >
            <el-form-item label="用户名:" prop="number">
              <el-input v-model="form.number" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="密码:" prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="请输入登录密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="验证码:" prop="verifyCode">
              <el-input
                ref="code"
                v-model="form.code"
                placeholder="请输入验证码"
                name="code"
                type="text"
                style="width: 70%"
              />
              <span class="verifyCode" @click="getKaptcha">
                <img :src="code_img" alt="" style="width: 100%; height: 30px">
              </span>
            </el-form-item>
            <div style="margin-bottom: 10px">
              <el-checkbox v-model="remember" label="记住密码" />
              <span class="SMSlogin" @click="SMSLogin">手机号登录</span>
            </div>
            <el-button type="info" plain @click="gotoRegister">注册</el-button>
            <el-button
              type="primary"
              :style="{ float: 'right' }"
              @click="handleLogin"
            >登录</el-button>
          </el-form>
        </div>
        <div v-else-if="isSMSLogin">
          <!-- 手机验证码登录表单 -->
          <el-form
            ref="SMSloginForm"
            :rules="rules"
            :model="SMSForm"
            label-width="80px"
            inline-message
          >
            <el-form-item label="手机号:" prop="number">
              <el-input v-model="SMSForm.number" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="验证码:" prop="code" class="passwordItem">
              <el-input
                v-model="SMSForm.code"
                type="code"
                placeholder="请输入验证码"
                class="SMSInput"
              />
              <div
                type="primary"
                class="getSMS"
                :class="SMSFlag && 'countgray'"
                @click="getSMS(SMSForm.number)"
              >
                <span v-if="!SMSFlag">获取验证码</span>
                <span v-else>{{ SMSCount }}秒</span>
              </div>
            </el-form-item>
            <div style="margin-bottom: 10px">
              <el-checkbox v-model="remember" label="记住密码" />
              <span class="SMSlogin" @click="gotoLogin">密码登录</span>
            </div>
            <el-button type="info" plain @click="gotoRegister">注册</el-button>
            <el-button
              type="primary"
              :style="{ float: 'right' }"
              @click="handleSMSLogin"
            >登录</el-button>
          </el-form>
        </div>
        <div v-else-if="isRegister">
          <!-- 注册表单 -->
          <el-form
            ref="regForm"
            :model="regForm"
            :rules="rules"
            :show-message="false"
            hide-required-asterisk
            label-width="70px"
            size="mini"
          >
            <!-- 老师学生公共部分 -->
            <el-form-item label="手机号" prop="number">
              <el-input v-model="regForm.number" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="regForm.password"
                type="password"
                placeholder="请输入登录密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="确认密码" prop="password2">
              <el-input
                v-model="regForm.password2"
                type="password"
                placeholder="请确认登录密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="regForm.name" placeholder="请输入姓名" />
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-select v-model="regForm.gender" placeholder="请选择性别">
                <el-option
                  v-for="item in genders"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="regForm.idCard" placeholder="请输入身份证号" />
            </el-form-item>
            <!-- ----------------------------- -->
            <template v-if="loginType === 'student'">
              <el-form-item label="学校名称" prop="schoolspecial" required>
                <el-input
                  v-model="regForm.schoolspecial"
                  placeholder="请输入学校名称"
                />
              </el-form-item>
              <el-form-item label="院系" required prop="institute">
                <el-input
                  v-model="regForm.institute"
                  placeholder="请输入院系"
                />
              </el-form-item>
              <el-form-item label="专业" required prop="major">
                <el-input v-model="regForm.major" placeholder="请输入专业" />
              </el-form-item>
              <el-form-item label="学号" required prop="sno">
                <el-input v-model="regForm.sno" placeholder="请输入学号" />
              </el-form-item>
              <el-form-item label="入学年月" required prop="intime">
                <el-date-picker
                  v-model="regForm.intime"
                  type="date"
                  placeholder="选择日期"
                  format="yyyy年MM月dd日"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
              <el-form-item label="毕业年月" required prop="outtime">
                <el-date-picker
                  v-model="regForm.outtime"
                  type="date"
                  placeholder="选择日期"
                  format="yyyy年MM月dd日"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
              <el-form-item label="类别" required prop="degree">
                <el-select v-model="regForm.degree" placeholder="请选择类别">
                  <el-option
                    v-for="item in degrees"
                    :key="item.id"
                    :label="item.label"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </template>
            <template v-else>
              <el-form-item label="学校名称" required prop="schoolid">
                <el-select
                  v-model="regForm.schoolid"
                  placeholder="请选择学校名称"
                >
                  <el-option
                    v-for="item in schools"
                    :key="item.id"
                    :label="item.label"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="职称" prop="title" required>
                <el-select v-model="regForm.title" placeholder="请选择职称">
                  <el-option
                    v-for="item in titles"
                    :key="item.id"
                    :label="item.label"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </template>
            <!-- 验证码公有 -->
            <el-form-item
              prop="code"
              style="position: relative"
              label="验证码"
              class="passwordItem"
            >
              <el-input
                ref="code"
                v-model="regForm.code"
                placeholder="请输入验证码"
                name="code"
                type="text"
                style="width: 60%"
                class="SMSInput"
              />
              <div
                type="primary"
                class="getSMS"
                :class="SMSFlag && 'countgray'"
                @click="getSMS(regForm.number)"
              >
                <span v-if="!SMSFlag">获取验证码</span>
                <span v-else>{{ SMSCount }}秒</span>
              </div>
            </el-form-item>
          </el-form>
          <el-button
            type="primary"
            size="mini"
            style="width: 100%"
            :loading="loading"
            @click="handleRegister"
          >完成</el-button>
          <el-button
            type="text"
            style="width: 100%"
            @click="gotoLogin"
          >已有账号？立即登录</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getKaptcha, verifyKaptcha } from '@/api/kaptcha';
import { registerMplx, sendSMS } from '@/api/user';
import {
  getCredential,
  getRemember,
  removeCredential,
  removeRemember,
  setCredential,
  setRemember
} from '@/utils/auth';
export default {
  data() {
    const validateNumber = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('手机号必填'));
      } else {
        if (!/^1[34578]\d{9}$/.test(value)) {
          callback(new Error('手机号格式错误'));
        } else {
          callback();
        }
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (value.length < 4) {
        callback(new Error('密码长度不少于6位'));
      } else {
        callback();
      }
    };
    const validateCode = (rule, value, callback) => {
      if (!value) {
        callback('请填写验证码');
      } else {
        callback();
      }
    };
    const validateRePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.regForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      isLogin: true, // login or register
      isRegister: false,
      isSMSLogin: false,

      SMSFlag: false,
      SMSCount: 60,

      loginType: 'teacher', // 'teacher' or 'student'
      remember: getRemember(),
      bgSrc: require('@/assets/bg.png'),
      loading: false,
      code_img: '',
      verify_token: '',
      rules: {
        number: [{ trigger: 'blur', validator: validateNumber }],
        password: [{ trigger: 'blur', validator: validatePassword }],
        code: [{ trigger: 'blur', validator: validateCode }],
        password2: [{ trigger: 'blur', validator: validateRePassword }],
        name: [{ required: true, trigger: 'blur' }],
        gender: [{ required: true, trigger: 'change' }],
        idCard: [
          {
            required: true,
            trigger: 'blur'
            // pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{7}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}$)/
          }
        ]
      },
      SMSForm: {
        number: '',
        code: ''
      },
      form: {
        number: '',
        password: ''
      },
      regForm: {
        // common
        number: '',
        password: '',
        password2: '',
        name: '',
        gender: undefined, // int
        idCard: '',
        code: '',
        // for teachers
        schoolid: undefined, // int
        title: undefined, // int
        // for students
        schoolspecial: '',
        institute: '',
        major: '',
        sno: '',
        intime: '', // YYYY-MM-DD 下同
        outtime: '',
        degree: undefined // int
      },
      degrees: [
        {
          id: 1,
          label: '小学'
        },
        {
          id: 2,
          label: '初中'
        },
        {
          id: 3,
          label: '高中'
        },
        {
          id: 4,
          label: '专科'
        },
        {
          id: 5,
          label: '本科'
        },
        {
          id: 6,
          label: '硕士'
        },
        {
          id: 7,
          label: '博士'
        }
      ],
      // id待确定
      genders: [
        {
          id: 1,
          label: '男'
        },
        {
          id: 2,
          label: '女'
        }
      ],
      titles: [
        {
          id: 1,
          label: '助教'
        },
        {
          id: 2,
          label: '讲师'
        },
        {
          id: 3,
          label: '副教授'
        },
        {
          id: 4,
          label: '教授'
        }
      ],
      schools: [
        {
          id: 1,
          label: '北京大学'
        },
        {
          id: 2,
          label: '清华大学'
        },
        {
          id: 3,
          label: '人民大学'
        },
        {
          id: 4,
          label: '复旦大学'
        },
        {
          id: 5,
          label: '浙江大学'
        },
        {
          id: 6,
          label: '南京大学'
        },
        {
          id: 7,
          label: '同济大学'
        }
      ]
    };
  },
  created() {
    // 记住密码
    if (getRemember()) {
      const kv = getCredential();
      if (kv) {
        this.form.number = kv.number;
        this.form.password = kv.password;
      }
    }
    this.getKaptcha();
  },
  methods: {
    // 手机验证码登录
    SMSLogin() {
      this.isLogin = false;
      this.isRegister = false;
      this.isSMSLogin = true;
    },
    handleTypeChange() {
      if (this.isLogin) {
        this.$refs['loginForm'].resetFields();
        this.getKaptcha();
      } else if (this.isSMSLogin) {
        this.$refs['SMSloginForm'].resetFields();
      } else {
        this.$refs['regForm'].resetFields();
      }
    },
    // 获取验证码，倒计时
    async getSMS(code) {
      if (!code) {
        this.$message.warning('请填写手机号');
        return;
      }
      if (!this.SMSFlag) {
        this.SMSFlag = true;
        this.SMSCount = 60;
        const setTimeoutS = setInterval(() => {
          this.SMSCount--;
          if (this.SMSCount <= 0) {
            clearInterval(setTimeoutS);
            this.SMSFlag = false;
          }
        }, 1000);
        console.log('code', code);
        const res = await sendSMS({ number: code });
        this.verify_token = res.headers['verify-token'];
      } else {
        return;
      }
    },
    gotoRegister() {
      this.isLogin = false;
      this.isSMSLogin = false;
      this.isRegister = true;
    },
    gotoLogin() {
      this.isLogin = true;
      this.isRegister = false;
      this.isSMSLogin = false;
      this.getKaptcha();
    },
    handleLogin() {
      this.$refs.loginForm.validate(async(valid) => {
        if (valid) {
          this.loading = true;
          const flag = await this.checkpicVerifyCode(this.form.code);
          if (flag) {
            this.$store
              .dispatch('user/myLogin', {
                type: this.loginType,
                form: {
                  username: this.form.number,
                  password: this.form.password
                }
              })
              .then(() => {
                if (this.remember) {
                  setRemember();
                  setCredential(this.form.number, this.form.password);
                } else {
                  removeCredential();
                  removeRemember();
                }
                // this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
                this.$router.push('/homepage');
                this.loading = false;
              })
              .catch((e) => {
                console.log(e);
                this.loading = false;
              });
          }
        } else {
          return false;
        }
      });
    },
    handleSMSLogin() {
      this.$refs.SMSloginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          this.$store
            .dispatch('user/mySMSLogin', {
              type: this.loginType,
              form: {
                username: this.SMSForm.number,
                verify_code: this.SMSForm.code
              },
              verify_token: this.verify_token
            })
            .then(() => {
              if (this.remember) {
                setRemember();
                setCredential(this.form.number, this.form.password);
              } else {
                removeCredential();
                removeRemember();
              }
              // this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
              this.$router.push('/homepage');
              this.loading = false;
            })
            .catch((e) => {
              console.log(e);
              this.loading = false;
            });
        } else {
          return false;
        }
      });
    },

    // 获取图形验证码
    getKaptcha() {
      getKaptcha()
        .then((res) => {
          this.verify_token = res.headers['verify-token'];
          this.code_img = window.URL.createObjectURL(res.data);
        })
        .catch((e) => {
          console.log(e);
        });
    },

    // 校验图形验证码
    async checkpicVerifyCode(code) {
      try {
        const verifyRes = await verifyKaptcha(code, this.verify_token);
        console.log(verifyRes);
        if (verifyRes.status !== 0) {
          this.$set(this, 'regForm.code', '');
          this.$message.warning('验证码不正确，请重新填写！');
          return false;
        }
        return true;
      } catch (e) {
        return false;
      } finally {
        this.loading = false;
      }
    },

    // 校验验证码
    async checkVerifyCode() {
      this.loading = true;
      try {
        // 注册
        const {
          number,
          password,
          name,
          gender,
          idCard,
          schoolid,
          title,
          schoolspecial,
          institute,
          major,
          sno,
          intime,
          outtime,
          degree,
          code
        } = this.regForm;
        let payload;
        if (this.loginType === 'student') {
          payload = {
            number,
            password,
            studentname: name,
            gender,
            idcard: idCard,
            schoolspecial,
            institute,
            major,
            sno,
            intime,
            outtime,
            degree,
            verify_token: this.verify_token,
            verify_code: code
          };
        } else {
          payload = {
            number,
            password,
            teachername: name,
            gender,
            idcard: idCard,
            schoolid,
            title,
            verify_token: this.verify_token,
            verify_code: code
          };
        }
        const regRes = await registerMplx(this.loginType, payload);
        console.log(regRes);
        if (regRes.status !== 0) {
          this.$message({
            message: '注册失败',
            type: 'error'
          });
          this.loading = false;
          return;
        }
        this.isLogin = true;
        this.$message({
          message: '注册成功，请登录！',
          type: 'success'
        });
        this.loading = false;
      } catch (error) {
        console.log(error);
        this.loading = false;
      }
    },
    // 注册
    handleRegister() {
      this.$refs.regForm.validate((valid) => {
        if (valid) {
          this.checkVerifyCode();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
.bg {
  height: 100%;
  background-size: 100% 100%;
  position: relative;
}
.front {
  position: absolute;
  left: 60%;
  top: 50%;
  transform: translateY(-50%);
  width: 340px;
}

.logo {
  position: absolute;
  left: 30px;
  top: 30px;
}
.header {
  color: white;
  text-align: center;
  font-size: 20px;
}

.main {
  background-color: white;
  padding: 20px 30px;
}
.el-radio-button {
  width: 50%;
}
.el-radio-button /deep/ .el-radio-button__inner {
  width: 100%;
}

.el-form-item {
  margin-bottom: 5px;
}

.verifyCode {
  display: inline-block;
  position: absolute;
  top: 0;
  z-index: 1000;
  width: 30%;
  height: 30px;
  border: 1px solid #a1a1a1;
  border-radius: 5px;
}

.SMSlogin {
  margin-left: 120px;
  color: #4091f7;
  font-size: 14px;
  cursor: pointer;
}

.passwordItem .SMSInput {
  width: 95px !important;
}
.passwordItem .getSMS {
  float: right;
  width: 90px;
  margin-top: 0px;
  margin-left: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #1c4494;
  height: 35px;
  color: #fff;
  border-radius: 5%;
  cursor: pointer;
}
.passwordItem .countgray {
  filter: grayscale(100%);
  cursor: not-allowed;
}
</style>
