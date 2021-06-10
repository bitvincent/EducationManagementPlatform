<template>
  <div class="app-container">
    <div v-if="isTeacher">
      <teacher :id="id" />
    </div>
    <div v-else>
      <student :id="id" />
    </div>
  </div>
</template>

<script>
import Teacher from '@/views/class/homework/teacher';
import Student from '@/views/class/homework/student';
export default {
  name: 'Homework',
  components: { Student, Teacher },
  props: {
    id: {
      type: String,
      default: ''
    }
  },
  computed: {
    isTeacher() {
      return this.$store.getters.isTeacher
    }
  }
}
</script>

<style scoped>

</style>
