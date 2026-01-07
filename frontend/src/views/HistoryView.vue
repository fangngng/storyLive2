<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useStoryStore } from '@/stores/story'

const router = useRouter()
const storyStore = useStoryStore()

onMounted(() => {
  storyStore.loadAllSessions().catch(error => {
    console.error('加载故事会话失败:', error)
  })
})

const viewStory = (sessionId: string) => {
  router.push(`/story/${sessionId}`)
}
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-900 to-black text-white">
    <div class="container mx-auto px-4 py-12">
      <div class="max-w-4xl mx-auto">
        <h1 class="text-3xl font-bold mb-8">故事历史</h1>
        
        <div v-if="storyStore.isLoading" class="flex items-center justify-center py-12">
          <div class="inline-block animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-purple-500"></div>
        </div>
        
        <div v-else-if="storyStore.storySessions.length === 0" class="text-center py-12">
          <p class="text-xl text-gray-400">还没有故事历史</p>
          <button
            @click="router.push('/')"
            class="mt-6 px-6 py-3 bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-700 hover:to-pink-700 rounded-lg font-semibold transition-all duration-300"
          >
            开始新故事
          </button>
        </div>
        
        <div v-else class="space-y-4">
          <div 
            v-for="session in storyStore.storySessions" 
            :key="session.id"
            @click="viewStory(session.id)"
            class="bg-gray-800/50 backdrop-blur-lg rounded-xl p-6 border border-gray-700 cursor-pointer hover:bg-gray-700/60 transition-colors duration-300"
          >
            <div class="flex justify-between items-start">
              <div>
                <h3 class="text-xl font-semibold mb-2">{{ session.title || '无标题故事' }}</h3>
                <p class="text-gray-400 text-sm">
                  开始于: {{ new Date(session.startedAt).toLocaleString() }}
                  <span v-if="session.endedAt"> | 结束于: {{ new Date(session.endedAt).toLocaleString() }}</span>
                </p>
                <p class="text-gray-500 text-sm mt-1">状态: {{ session.status }}</p>
              </div>
              <span class="px-3 py-1 bg-purple-900/50 rounded-full text-sm">
                {{ session.nodeCount || '未知' }} 个节点
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
</template>