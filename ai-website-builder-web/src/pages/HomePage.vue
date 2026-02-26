<template>
  <a-pagination :total="50" show-size-changer />
</template>

<script setup lang="ts">
import { healthCheck } from '@/api/healthController.ts'

healthCheck().then((res) => {
  console.log(res)
})
</script>

<style scoped></style>
