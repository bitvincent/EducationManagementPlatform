<template>
  <div class="app-container">
    <div class="header-container">
      <h3 class="header"> {{ isTeacher?'我发布的课程':'我的课程' }}</h3>
      <el-button
        v-if="isTeacher"
        type="primary"
        class="button"
        @click="$router.push({name: 'PublishClass'})"
      >
        发布课程
      </el-button>
    </div>
    <div class="card-container">
      <class-card v-for="(item, index) in list" :key="index" :data="item" class="card" />
    </div>
  </div>
</template>

<script>
import ClassCard from '@/components/ClassCard'
import { getPublishedClass, getSelectedClass } from '@/api/classes'

export default {
  name: 'MyClassList',
  components: { ClassCard },
  data() {
    return {
      list: []
    }
  },
  computed: {
    isTeacher() {
      return this.$store.getters.loggedIn && this.$store.getters.isTeacher
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      const getClass = this.isTeacher ? getPublishedClass : getSelectedClass
      getClass().then((res) => { this.list = res.data }).catch((err) => { console.log(err) })
    }
  }
}
</script>

<style scoped>
.header-container {
  display: flex;
  justify-content: space-between;
  align-items:center;
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

.button {
  background-color: #10429a;
  border-color: #10429a;
}
</style>
