<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStoryStore } from '@/stores/story'

const router = useRouter()
const storyStore = useStoryStore()

const initialPrompt = ref('开始一个冒险故事')
const isLoading = ref(false)

const startNewStory = async () => {
  if (!initialPrompt.value.trim()) {
    alert('请输入故事的初始提示')
    return
  }
  
  try {
    isLoading.value = true
    await storyStore.startNewStory(initialPrompt.value)
    // 跳转到故事页面
    if (storyStore.currentSessionId) {
      router.push(`/story/${storyStore.currentSessionId}`)
    }
  } catch (error) {
    console.error('启动故事失败:', error)
    alert('启动故事失败，请检查后端服务是否运行')
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-purple-900 via-blue-900 to-indigo-900 text-white">
    <div class="container mx-auto px-4 py-12">
      <div class="max-w-3xl mx-auto text-center">
        <h1 class="text-5xl font-bold mb-6 bg-clip-text text-transparent bg-gradient-to-r from-pink-400 to-purple-300">
          AI 故事生成系统
        </h1>
        <p class="text-xl text-gray-300 mb-12">
          体验由AI驱动的互动故事，每个选择都将影响故事的走向
        </p>
        
        <div class="bg-gray-800/30 backdrop-blur-lg rounded-2xl p-8 mb-12 border border-gray-700">
          <h2 class="text-2xl font-semibold mb-6">开始新的故事</h2>
          
          <div class="mb-6">
            <input
              v-model="initialPrompt"
              type="text"
              placeholder="输入故事的初始提示，例如：魔法学院的新生..."
              class="w-full px-4 py-3 rounded-lg bg-gray-700/50 border border-gray-600 focus:border-purple-500 focus:outline-none text-white placeholder-gray-400"
              @keyup.enter="startNewStory"
            />
          </div>
          
          <button
            @click="startNewStory"
            :disabled="isLoading"
            class="px-8 py-3 bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-700 hover:to-pink-700 rounded-lg font-semibold transition-all duration-300 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span v-if="!isLoading">开始故事</span>
            <span v-else>生成中...</span>
          </button>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
          <div class="bg-gray-800/30 backdrop-blur-lg rounded-xl p-6 border border-gray-700">
            <div class="text-3xl mb-3">✨</div>
            <h3 class="text-lg font-semibold mb-2">打字机效果</h3>
            <p class="text-gray-400 text-sm">故事内容以打字机效果逐字显示，增强阅读体验</p>
          </div>
          
          <div class="bg-gray-800/30 backdrop-blur-lg rounded-xl p-6 border border-gray-700">
            <div class="text-3xl mb-3">🧠</div>
            <h3 class="text-lg font-semibold mb-2">AI故事生成</h3>
            <p class="text-gray-400 text-sm">使用AI模型生成连贯的故事内容和可选分支</p>
          </div>
          
          <div class="bg-gray-800/30 backdrop-blur-lg rounded-xl p-6 border border-gray-700">
            <div class="text-3xl mb-3">🖼️</div>
            <h3 class="text-lg font-semibold mb-2">动态图片生成</h3>
            <p class="text-gray-400 text-sm">根据故事内容生成匹配的背景图片</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
</template>