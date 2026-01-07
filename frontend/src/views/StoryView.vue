<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStoryStore } from '@/stores/story'
import TypewriterEffect from '@/components/TypewriterEffect.vue'
import StoryOption from '@/components/StoryOption.vue'

const route = useRoute()
const router = useRouter()
const storyStore = useStoryStore()

const sessionId = computed(() => route.params.id as string)

const handleOptionSelect = async (optionId: string) => {
  try {
    await storyStore.continueStory(optionId)
  } catch (error) {
    console.error('继续故事失败:', error)
    alert('继续故事失败，请重试')
  }
}

onMounted(() => {
  if (sessionId.value) {
    // 加载故事历史
    storyStore.loadStoryHistory(sessionId.value).catch(error => {
      console.error('加载故事历史失败:', error)
      // 如果加载失败，返回首页
      router.push('/')
    })
  }
})
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-900 to-black text-white">
    <div v-if="storyStore.isLoading" class="flex items-center justify-center min-h-screen">
      <div class="text-center">
        <div class="inline-block animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-purple-500 mb-4"></div>
        <p>AI正在创作故事...</p>
      </div>
    </div>
    
    <div v-else-if="storyStore.currentStory" class="container mx-auto px-4 py-8">
      <!-- 背景图片 -->
      <div class="relative h-96 rounded-2xl overflow-hidden mb-8">
        <img 
          :src="storyStore.currentStoryImage" 
          alt="故事背景" 
          class="w-full h-full object-cover"
          @error="e => (e.target as HTMLImageElement).src='https://placehold.co/800x600?text=Story+Image'"
        />
        <div class="absolute inset-0 bg-gradient-to-t from-black/80 to-transparent"></div>
      </div>
      
      <!-- 故事内容 -->
      <div class="max-w-3xl mx-auto">
        <div class="bg-gray-800/50 backdrop-blur-lg rounded-2xl p-8 mb-8 border border-gray-700">
          <TypewriterEffect 
            :text="storyStore.currentStoryContent" 
            :speed="30"
            @finished="() => {}"
          />
        </div>
        
        <!-- 选项 -->
        <div v-if="storyStore.currentStoryOptions.length > 0" class="space-y-4 mb-8">
          <h3 class="text-xl font-semibold mb-4">选择你的下一步:</h3>
          <div v-for="option in storyStore.currentStoryOptions" :key="option.id" class="space-y-3">
            <StoryOption
              :text="option.text"
              :loading="storyStore.isLoading"
              @select="handleOptionSelect(option.id)"
            />
          </div>
        </div>
        
        <div v-else class="text-center py-8">
          <p class="text-lg mb-4">故事已结束</p>
          <button
            @click="router.push('/')"
            class="px-6 py-3 bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-700 hover:to-pink-700 rounded-lg font-semibold transition-all duration-300"
          >
            开始新故事
          </button>
        </div>
      </div>
    </div>
    
    <div v-else class="flex items-center justify-center min-h-screen">
      <p>未找到故事</p>
    </div>
  </div>
</template>
</template>