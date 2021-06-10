<template>
  <div class="main">
    <el-row style="width: 1200px; margin-top: 20px">
      <el-col :span="6"><search-class /></el-col>
      <el-col :span="12" style="text-align: center">
        <el-image class="firstImg" :src="imgSrc" @click="gotoFirst" />
      </el-col>
      <el-col :span="6"><avatar /></el-col>
    </el-row>
    <h3 class="header">编辑推荐</h3>
    <div class="card-container">
      <class-card v-for="(item, index) in remains" :key="index" :data="item" class="card" />
    </div>
  </div>
</template>

<script>
import searchClass from './search'
import avatar from './avatar'
import ClassCard from '@/components/ClassCard'

export default {
  components: {
    ClassCard,
    searchClass,
    avatar
  },
  computed: {
    firstImg() {
      if (this.$store.state.homepage.classes.length > 0) {
        return this.$store.state.homepage.classes[0]
      } else {
        return undefined
      }
    },
    imgSrc() {
      if (this.firstImg) {
        const url = this.firstImg.classpic
        if (url.startsWith('http')) return url
        return 'http://' + this.firstImg.classpic
      } else {
        return ''
      }
    },
    remains() {
      if (this.$store.state.homepage.classes.length > 0) {
        return this.$store.state.homepage.classes.slice(1)
      } else {
        return []
      }
    }
  },
  created() {
    this.$store.dispatch('homepage/getClasses')
  },
  methods: {
    gotoFirst() {
      this.$router.push({ name: 'ClassDetail', params: { id: this.firstImg.classid }})
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
    width: 1200px;
    margin: 0 auto;
}

.firstImg {
  width: 97%;
  height: 300px;
  cursor: pointer;
  border-radius: 10px;
}

.header {
  font-size: 22px;
  font-weight: 500;
}

.card-container {
  display: flex;
  flex-wrap: wrap;
}

.card {
  margin: 0 10px 20px;
}
.card:nth-child(6n),.card:first-child{
  margin-left: 0;
}
.card:nth-child(5n) {
  margin-right: 0;
}
</style>>

