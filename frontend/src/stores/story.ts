import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

// 定义类型
interface StoryOption {
  id: string
  text: string
  nextNodeId: string
}

interface StoryNode {
  id: string
  sessionId: string
  content: string
  imageUrl: string
  options: StoryOption[]
  createdAt: string
}

interface AIConfiguration {
  id: string
  modelName: string
  temperature: number
  maxTokens: number
  topP: number
  isDefault: boolean
}

export const useStoryStore = defineStore('story', () => {
  // 状态
  const currentStory = ref<StoryNode | null>(null)
  const storyHistory = ref<StoryNode[]>([])
  const storySessions = ref<any[]>([])
  const aiConfigurations = ref<AIConfiguration[]>([])
  const currentSessionId = ref<string | null>(null)
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  // 计算属性
  const hasCurrentStory = computed(() => currentStory.value !== null)
  const currentStoryOptions = computed(() => currentStory.value?.options || [])
  const currentStoryContent = computed(() => currentStory.value?.content || '')
  const currentStoryImage = computed(() => currentStory.value?.imageUrl || '')

  // API基础URL
  const API_BASE_URL = 'http://localhost:8080/api'

  // 方法
  const startNewStory = async (initialPrompt: string, aiConfigId?: string) => {
    try {
      isLoading.value = true
      error.value = null
      
      const response = await axios.post<StoryNode>(`${API_BASE_URL}/story/start`, {
        initialPrompt,
        aiConfigId
      })
      
      currentStory.value = response.data
      currentSessionId.value = response.data.sessionId
      storyHistory.value = [response.data]
      
      return response.data
    } catch (err: any) {
      error.value = err.message || '启动故事时发生错误'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const continueStory = async (optionId: string) => {
    try {
      isLoading.value = true
      error.value = null
      
      if (!currentSessionId.value) {
        throw new Error('没有活动的故事会话')
      }
      
      const response = await axios.post<StoryNode>(`${API_BASE_URL}/story/continue`, {
        sessionId: currentSessionId.value,
        optionId
      })
      
      currentStory.value = response.data
      storyHistory.value.push(response.data)
      
      return response.data
    } catch (err: any) {
      error.value = err.message || '继续故事时发生错误'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const loadStoryHistory = async (sessionId: string) => {
    try {
      isLoading.value = true
      error.value = null
      
      const response = await axios.get<StoryNode[]>(`${API_BASE_URL}/story/history/${sessionId}`)
      
      storyHistory.value = response.data
      if (response.data.length > 0) {
        currentStory.value = response.data[response.data.length - 1]
        currentSessionId.value = sessionId
      }
      
      return response.data
    } catch (err: any) {
      error.value = err.message || '加载故事历史时发生错误'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const loadAllSessions = async () => {
    try {
      isLoading.value = true
      error.value = null
      
      const response = await axios.get(`${API_BASE_URL}/story/sessions`)
      
      storySessions.value = response.data
      
      return response.data
    } catch (err: any) {
      error.value = err.message || '加载故事会话时发生错误'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const loadAIConfigurations = async () => {
    try {
      isLoading.value = true
      error.value = null
      
      const response = await axios.get<AIConfiguration[]>(`${API_BASE_URL}/config/ai`)
      
      aiConfigurations.value = response.data
      
      return response.data
    } catch (err: any) {
      error.value = err.message || '加载AI配置时发生错误'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const updateAIConfiguration = async (config: AIConfiguration) => {
    try {
      isLoading.value = true
      error.value = null
      
      const response = await axios.put<AIConfiguration>(`${API_BASE_URL}/config/ai/${config.id}`, config)
      
      // 更新本地配置列表
      const index = aiConfigurations.value.findIndex(c => c.id === config.id)
      if (index !== -1) {
        aiConfigurations.value[index] = response.data
      }
      
      return response.data
    } catch (err: any) {
      error.value = err.message || '更新AI配置时发生错误'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const resetStory = () => {
    currentStory.value = null
    storyHistory.value = []
    currentSessionId.value = null
  }

  return {
    // 状态
    currentStory,
    storyHistory,
    storySessions,
    aiConfigurations,
    currentSessionId,
    isLoading,
    error,
    
    // 计算属性
    hasCurrentStory,
    currentStoryOptions,
    currentStoryContent,
    currentStoryImage,
    
    // 方法
    startNewStory,
    continueStory,
    loadStoryHistory,
    loadAllSessions,
    loadAIConfigurations,
    updateAIConfiguration,
    resetStory
  }
})