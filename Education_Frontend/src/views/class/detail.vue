<template>
  <div class="app-container">
    <h3 class="header">课程详情</h3>
    <el-card style="width: 100%; height: 315px">
      <el-row :gutter="20">
        <el-col :span="10">
          <el-image style="width: 100%; height: 265px; border-radius: 4px" :src="data.classpic" alt="classpic" />
        </el-col>
        <el-col :span="14">
          <el-tooltip :content="data.name" placement="top" :disabled="!nameOverflow">
            <h3 ref="name" class="header hide-overflow" style="margin-top: 0; width: 80%">{{ data.name }}</h3>
          </el-tooltip>
          <div class="info-container">
            <div>
              <div>开课时间：
                {{ parseTime(data.starttime, '{y}年{m}月{d}日') }} - {{ parseTime(data.endtime, '{y}年{m}月{d}日') }}
              </div>
              <div style="margin-top: 8px">学时安排： 每周{{ data.learnhours }}学时</div>
            </div>
            <div :style="{color: end ? '#F56C6C' : '#67C23A'}">
              {{ progress }}
            </div>
          </div>
          <div style="margin: 25px 0" class="minor"> 已有{{ data.number }}人参加</div>
          <div v-if="isStudent">
            <el-button v-if="!selected" type="primary" class="button" @click="selectClass">选 课</el-button>
            <div v-else>
              <el-button
                type="primary"
                class="button"
                @click="$router.push({ name:'Homework', params:{ id } })"
              >
                我的作业
              </el-button>
              <el-button
                type="primary"
                class="button"
                style="margin-left: 20px"
                @click="toCertificateDetail"
              >
                查看证书
              </el-button>
              <el-button
                type="primary"
                class="button"
                style="margin-left: 20px"
                @click="toLearnProcess"
              >
                查看历史过程
              </el-button>
            </div>
          </div>
          <div v-if="isClassOwner">
            <el-button
              type="primary"
              class="button"
              @click="$router.push({ name:'Homework', params:{ id } })"
            >
              课程作业
            </el-button>
            <el-button
              type="primary"
              class="button"
              style="margin-left: 20px"
              @click="toPublishCertificate"
            >发布证书</el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-row :gutter="20" style="margin-top: 30px; margin-bottom: 20px">
      <el-col :span="18">
        <el-card style="min-height: 266px">
          <div style="position: relative">
            <div class="tab">课程详情</div>
            <div class="tab-bar" style="width: 72px" />
          </div>
          <div class="intro-container" style="margin-top: 30px">
            <div>{{ data.description }}</div>
            <div style="text-align: right">—— 课程团队</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="min-height: 266px">
          <el-image fit="scale-down" :src="data.schoollogo" alt="logo" style="height: 100px; width: 100%" />
          <el-divider />
          <div class="teacher-header">授课老师</div>
          <div style="display:flex; align-items: center">
            <!-- TODO: src -->
            <el-avatar :size="50" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
            <div style="margin-left: 12px; width: 160px">
              <el-tooltip :content="data.teachername" placement="top" :disabled="!teacherNameOverflow">
                <div ref="teacherName" class="hide-overflow" style="font-weight: 500">{{ data.teachername }}</div>
              </el-tooltip>
              <div style="font-size: 0.8em; color: #909399; margin-top: 6px">{{ titles[data.title - 1] }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getClassInfo, getStudentClassInfo, selectClass } from '@/api/classes'
import { parseTime } from '@/utils'

export default {
  name: 'ClassDetail',
  props: {
    id: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      data: {
        name: '加载中',
        classpic: '',
        starttime: new Date(0),
        endtime: new Date(0),
        learnhours: 0,
        number: 0,
        teacherid: -2,
        description: '加载中',
        schoollogo: '',
        teachername: '加载中',
        title: 4
      },
      end: false,
      progress: '',
      nameOverflow: false,
      teacherNameOverflow: false,
      titles: ['助教', '讲师', '副教授', '教授']
    }
  },
  computed: {
    isTeacher() {
      return this.$store.getters.loggedIn && this.$store.getters.isTeacher
    },
    isStudent() {
      return this.$store.getters.loggedIn && this.$store.getters.isStudent
    },
    teacherId() {
      return this.$store.getters.teacherId
    },
    selected() {
      return this.data.select === 1
    },
    isClassOwner() {
      return this.isTeacher && this.data.teacherid === this.teacherId
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      const getInfo = this.isStudent ? getStudentClassInfo : getClassInfo
      getInfo(this.id).then(res => {
        res.data.starttime = new Date(res.data.starttime)
        res.data.endtime = new Date(res.data.endtime)
        this.data = res.data
        this.$nextTick(() => {
          this.computeProgress()
          this.checkOverflow()
        })
      }).catch(err => { console.log(err) })
    },
    computeProgress() {
      const start = new Date(new Date(this.data.starttime).setHours(0, 0, 0, 0))
      const end = new Date(new Date(this.data.endtime).setHours(23, 59, 59, 999))
      const totalWeek = (end.getTime() - start.getTime()) / 1000 / 60 / 60 / 24 / 7
      const passedWeek = (new Date().getTime() - start.getTime()) / 1000 / 60 / 60 / 24 / 7
      if (passedWeek < 0) {
        this.progress = '课程暂未开始'
      } else if (passedWeek > totalWeek) {
        this.end = true
        this.progress = '课程已结束'
      } else {
        this.progress = `进行至第${Math.ceil(passedWeek)}周，共${Math.ceil(totalWeek)}周`
      }
    },
    checkOverflow() {
      const nameElx = this.$refs['name']
      const teacherNameElx = this.$refs['teacherName']
      this.nameOverflow = nameElx && nameElx.scrollWidth > nameElx.offsetWidth
      this.teacherNameOverflow = teacherNameElx && teacherNameElx.scrollWidth > teacherNameElx.offsetWidth
    },
    selectClass() {
      if (!this.isStudent || this.data.select === 1) return
      this.$confirm('确定选修该课程?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        selectClass(this.id).then(() => {
          this.$notify.success({
            title: '成功',
            message: '选课成功',
            duration: 5 * 1000
          })
          this.fetchData()
        }).catch(err => { console.log(err) })
      })
    },
    toCertificateDetail() {
      this.$router.push(`/certificate-detail/${this.id}`)
    },
    toPublishCertificate() {
      const path = this.$router.history.current.path;
      this.$router.push(`${path}/publish-certificate`)
    },
    toLearnProcess() {
      const path = this.$router.history.current.path;
      this.$router.push(`${path}/learn-process`)
    },
    parseTime
  }
}
</script>

<style scoped>
.header {
  font-size: 22px;
  font-weight: 500;
}

.hide-overflow {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.info-container {
  width: 85%;
  height: 85px;
  display: flex;
  justify-content: space-between;
  align-items:center;
  background-color: #f4f4f5;
  color: #909399;
  padding: 0 10px 0 20px;
  font-size: 0.9em;
}

.minor {
  font-size:0.9em;
  line-height: 16px;
  color: #909399;
}

.button {
  width: 200px;
  height: 40px;
  background-color: #10429a;
  border-color: #10429a;
}

.button.disabled {
  background-color: #c8c9cc;
  border-color: #c8c9cc;
}

.tab {
  position: relative;
  padding-bottom: 10px;
  font-size: 18px;
  font-weight: 500;
  color: #67C23A;
}
.tab::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 2px;
  background-color: #dfe4ed;
  z-index: 1;
}
.tab-bar {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 4px;
  background-color: #67C23A;
  z-index: 1;
  -webkit-transition: -webkit-transform 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  transition: -webkit-transform 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  transition: transform 0.3s cubic-bezier(0.645, 0.045, 0.355, 1), -webkit-transform 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  list-style: none;
}

.intro-container {
  padding: 20px;
  white-space: pre-wrap;
  word-break: break-word;
  font-size: 0.9em;
  line-height: 22px;
  color: #606266;
  background-color: #f0f3ed;
}

::v-deep .el-divider--horizontal {
  margin: 10px auto;
}

.teacher-header {
  position: relative;
  margin-top: 20px;
  margin-bottom: 20px;
  padding-left: 16px;
  font-size: 18px;
  font-weight: 400;
}

.teacher-header::before {
  position: absolute;
  left: 0;
  content: '';
  background-color: #67C23A;
  width: 5px;
  height: 20px;
}
</style>
