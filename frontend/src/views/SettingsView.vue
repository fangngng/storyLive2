<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useStoryStore } from '@/stores/story'

const storyStore = useStoryStore()

// 用于编辑配置的临时数据
const editingConfig = ref<any>(null)
const isEditing = ref(false)

onMounted(() => {
  storyStore.loadAIConfigurations().catch(error => {
    console.error('加载AI配置失败:', error)
  })
})

const startEditing = (config: any) => {
  editingConfig.value = { ...config }
  isEditing.value = true
}

const saveConfig = async () => {
  if (editingConfig.value) {
    try {
      await storyStore.updateAIConfiguration(editingConfig.value)
      isEditing.value = false
      editingConfig.value = null
    } catch (error) {
      console.error('更新AI配置失败:', error)
    }
  }
}

const cancelEdit = () => {
  isEditing.value = false
  editingConfig.value = null
}
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-900 to-black text-white">
    <div class="container mx-auto px-4 py-12">
      <div class="max-w-4xl mx-auto">
        <h1 class="text-3xl font-bold mb-8">设置</h1>
        
        <div v-if="storyStore.isLoading" class="flex items-center justify-center py-12">
          <div class="inline-block animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-purple-500"></div>
        </div>
        
        <div v-else class="space-y-8">
          <div class="bg-gray-800/50 backdrop-blur-lg rounded-2xl p-8 border border-gray-700">
            <h2 class="text-2xl font-semibold mb-6">AI模型配置</h2>
            
            <div v-if="storyStore.aiConfigurations.length === 0" class="text-center py-8">
              <p class="text-gray-400">暂无AI配置</p>
            </div>
            
            <div v-else class="space-y-6">
              <div 
                v-for="config in storyStore.aiConfigurations" 
                :key="config.id"
                class="p-6 bg-gray-700/30 rounded-xl border border-gray-600"
              >
                <div class="flex justify-between items-start mb-4">
                  <div>
                    <h3 class="text-lg font-semibold flex items-center">
                      {{ config.modelName }}
                      <span 
                        v-if="config.isDefault" 
                        class="ml-2 px-2 py-1 bg-purple-600 text-xs rounded-full"
                      >
                        默认
                      </span>
                    </h3>
                    <p class="text-gray-400 text-sm">ID: {{ config.id }}</p>
                  </div>
                  <button 
                    @click="startEditing(config)"
                    class="px-4 py-2 bg-gray-600 hover:bg-gray-500 rounded-lg text-sm transition-colors"
                  >
                    编辑
                  </button>
                </div>
                
                <div class="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                  <div>
                    <p class="text-gray-400">Temperature</p>
                    <p>{{ config.temperature }}</p>
                  </div>
                  <div>
                    <p class="text-gray-400">Max Tokens</p>
                    <p>{{ config.maxTokens }}</p>
                  </div>
                  <div>
                    <p class="text-gray-400">Top P</p>
                    <p>{{ config.topP }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 编辑配置模态框 -->
          <div v-if="isEditing && editingConfig" class="fixed inset-0 bg-black/70 flex items-center justify-center p-4 z-50">
            <div class="bg-gray-800 rounded-2xl p-8 w-full max-w-md border border-gray-700">
              <h3 class="text-xl font-semibold mb-6">编辑AI配置</h3>
              
              <div class="space-y-4">
                <div>
                  <label class="block text-sm font-medium mb-2">模型名称</label>
                  <input
                    v-model="editingConfig.modelName"
                    type="text"
                    class="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded-lg text-white"
                  />
                </div>
                
                <div>
                  <label class="block text-sm font-medium mb-2">Temperature: {{ editingConfig.temperature }}</label>
                  <input
                    v-model="editingConfig.temperature"
                    type="range"
                    min="0"
                    max="2"
                    step="0.1"
                    class="w-full"
                  />
                </div>
                
                <div>
                  <label class="block text-sm font-medium mb-2">Max Tokens: {{ editingConfig.maxTokens }}</label>
                  <input
                    v-model.number="editingConfig.maxTokens"
                    type="number"
                    min="1"
                    max="4096"
                    class="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded-lg text-white"
                  />
                </div>
                
                <div>
                  <label class="block text-sm font-medium mb-2">Top P: {{ editingConfig.topP }}</label>
                  <input
                    v-model="editingConfig.topP"
                    type="range"
                    min="0"
                    max="1"
                    step="0.05"
                    class="w-full"
                  />
                </div>
                
                <div class="flex items-center">
                  <input
                    v-model="editingConfig.isDefault"
                    type="checkbox"
                    id="isDefault"
                    class="mr-2"
                  />
                  <label for="isDefault">设为默认配置</label>
                </div>
              </div>
              
              <div class="flex space-x-3 mt-6">
                <button
                  @click="saveConfig"
                  class="flex-1 px-4 py-2 bg-purple-600 hover:bg-purple-700 rounded-lg transition-colors"
                >
                  保存
                </button>
                <button
                  @click="cancelEdit"
                  class="flex-1 px-4 py-2 bg-gray-600 hover:bg-gray-700 rounded-lg transition-colors"
                >
                  取消
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
</template>