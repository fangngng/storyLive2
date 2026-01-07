<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'

const props = defineProps<{
  text: string
  speed?: number
}>()

const emit = defineEmits<{
  finished: []
}>()

const displayedText = ref('')
const isTyping = ref(false)

const typeText = () => {
  if (!props.text) {
    displayedText.value = ''
    emit('finished')
    return
  }

  isTyping.value = true
  displayedText.value = ''
  
  let i = 0
  const speed = props.speed || 20 // 默认每20毫秒一个字符
  
  const type = () => {
    if (i < props.text.length) {
      displayedText.value += props.text.charAt(i)
      i++
      setTimeout(type, speed)
    } else {
      isTyping.value = false
      emit('finished')
    }
  }
  
  type()
}

// 当文本变化时重新开始打字效果
watch(() => props.text, () => {
  typeText()
}, { immediate: true })

onMounted(() => {
  typeText()
})
</script>

<template>
  <div class="typewriter-container">
    <span class="typewriter-text">{{ displayedText }}</span>
    <span v-if="isTyping" class="typewriter-cursor">|</span>
  </div>
</template>

<style scoped>
.typewriter-cursor {
  animation: blink 1s infinite;
  margin-left: 2px;
}

@keyframes blink {
  0%, 50% { opacity: 1; }
  51%, 100% { opacity: 0; }
}
</style>
</template>