<template>
  <el-card
    style="border: none"
    :body-style="{ padding: '0px', cursor: 'pointer', width: '224px', height: '306px' }"
    @click.native="$router.push({name: 'ClassDetail', params: {id: data.classid}})"
  >
    <el-image :src="data.classpic" alt="classpic" class="image" />
    <div style="padding: 10px">
      <div style="height: 80px">
        <h3 :id="'name' + data.classid" style="margin: 5px 0;">{{ data.classname }}</h3>
        <span class="minor">{{ $store.state.homepage.schools[data.schoolid - 1].label }}</span>
      </div>
      <span class="minor">{{ data.number }}人参加 </span>
      <el-divider />
      <span :id="'intro' + data.classid" class="minor"> {{ data.description }}</span>
    </div>
  </el-card>
</template>

<script>
import clamp from 'clamp-js'

export default {
  name: 'ClassCard',
  props: {
    data: {
      type: Object,
      required: true,
      default: () => ({ classid: -1, classname: '', schoolid: -1, number: 0, description: '', classpic: '' })
    }
  },
  mounted() {
    const name = document.getElementById('name' + this.data.classid)
    const intro = document.getElementById('intro' + this.data.classid)
    if (name) clamp(name, { clamp: 2 })
    if (intro) clamp(intro, { clamp: 2 })
  }
}
</script>

<style scoped>
.image {
  width: 100%;
  height: 126px;
  display: block;
  border-radius: 4px;
}

.minor {
  display: block;
  font-size:0.9em;
  line-height: 16px;
  color: #909399;
}

::v-deep .el-divider--horizontal {
  margin: 10px auto;
}
</style>
