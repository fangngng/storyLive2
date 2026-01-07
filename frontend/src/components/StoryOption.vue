<script setup lang="ts">
import { ref, withDefaults } from 'vue'

interface Props {
  text: string
  disabled?: boolean
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  disabled: false,
  loading: false
})

const emit = defineEmits<{
  select: []
}>()

const handleClick = () => {
  if (!props.disabled && !props.loading) {
    emit('select')
  }
}
</script>

<template>
  <button
    @click="handleClick"
    :disabled="disabled || loading"
    class="w-full p-4 text-left bg-gray-700/50 hover:bg-gray-600/70 rounded-lg border border-gray-600 hover:border-purple-500 transition-all duration-300 text-white disabled:opacity-50 disabled:cursor-not-allowed group"
  >
    <div class="flex items-center justify-between">
      <span>{{ text }}</span>
      <span v-if="loading" class="ml-2">
        <svg class="animate-spin h-5 w-5 text-purple-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
      </span>
      <span v-else class="ml-2 transform group-hover:translate-x-1 transition-transform">
        →
      </span>
    </div>
  </button>
</template>
</template>