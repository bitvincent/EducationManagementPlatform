<template>
  <detail :id="id" correct />
</template>

<script>
import Detail from '@/views/class/homework/detail';
export default {
  name: 'Correct',
  components: { Detail },
  props: {
    id: {
      type: String,
      default: ''
    }
  }
}
</script>

<style scoped>

</style>
