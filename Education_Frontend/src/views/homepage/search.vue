<template>
  <div class="ctn">
    <p class="hd">查找课程</p>
    <el-input v-model="classname" placeholder="请输入课程名称" clearable />
    <el-select v-model="schoolid" placeholder="请选择开课学校" class="mg-top" clearable>
      <el-option
        v-for="item in schools"
        :key="item.id"
        :label="item.label"
        :value="item.id"
      />
    </el-select>
    <el-select v-model="category" placeholder="请选择课程分类" class="mg-top" clearable>
      <el-option
        v-for="item in classCategory"
        :key="item.id"
        :label="item.label"
        :value="item.id"
      />
    </el-select>
    <el-button type="primary" :loading="loading" class="mg-top" @click="handleSearch">查找</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      classname: '',
      category: undefined, // become int after selection
      schoolid: undefined,
      loading: false,
      schools: [{
        id: 1,
        label: '北京大学'
      }, {
        id: 2,
        label: '清华大学'
      }, {
        id: 3,
        label: '人民大学'
      }, {
        id: 4,
        label: '复旦大学'
      }, {
        id: 5,
        label: '浙江大学'
      }, {
        id: 6,
        label: '南京大学'
      }, {
        id: 7,
        label: '同济大学'
      }],
      classCategory: [{
        id: 1,
        label: '哲学'
      }, {
        id: 2,
        label: '经济学'
      }, {
        id: 3,
        label: '法学'
      }, {
        id: 4,
        label: '教育学'
      }, {
        id: 5,
        label: '文学'
      }, {
        id: 6,
        label: '历史学'
      }, {
        id: 7,
        label: '理学'
      }, {
        id: 8,
        label: '工学'
      }, {
        id: 9,
        label: '农学'
      }, {
        id: 10,
        label: '医学'
      }, {
        id: 11,
        label: '军事学'
      }, {
        id: 12,
        label: '管理学'
      }, {
        id: 13,
        label: '艺术学'
      }]
    };
  },
  methods: {
    handleSearch() {
      this.loading = true
      const cond = {
        classname: this.classname,
        category: this.category || '', // empty
        schoolid: this.schoolid || ''
      }
      this.$store.dispatch('homepage/searchClasses', cond).then((res) => {
        if (res.length === 0) {
          this.$message('未找到相关课程')
        }
      }).finally(() => {
        this.loading = false
      })
    }
  }

}
</script>

<style lang="scss" scoped>
.ctn {
  height: 300px;
  padding: 20px;
  background-color: white;
  border-radius: 10px;
}
.hd {
  color:blue;
  margin: 0 ;
  margin-bottom: 40px;
}
.mg-top {
  margin-top: 10px;
  width: 100%;
  border-radius:20px;
}
</style>>

