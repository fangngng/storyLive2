<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header class="bg-gray-900/80 backdrop-blur-md border-b border-gray-800 sticky top-0 z-40">
    <div class="container mx-auto px-4">
      <nav class="flex items-center justify-between py-4">
        <RouterLink to="/" class="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-pink-400 to-purple-300">
          StoryLive2
        </RouterLink>
        
        <div class="hidden md:flex space-x-8">
          <RouterLink 
            to="/" 
            class="text-gray-300 hover:text-white transition-colors"
            active-class="text-purple-400"
          >
            首页
          </RouterLink>
          <RouterLink 
            to="/history" 
            class="text-gray-300 hover:text-white transition-colors"
            active-class="text-purple-400"
          >
            历史记录
          </RouterLink>
          <RouterLink 
            to="/settings" 
            class="text-gray-300 hover:text-white transition-colors"
            active-class="text-purple-400"
          >
            设置
          </RouterLink>
        </div>
        
        <div class="flex md:hidden">
          <!-- 移动端菜单按钮 -->
          <button class="text-gray-300 hover:text-white">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
        </div>
      </nav>
    </div>
  </header>

  <main>
    <RouterView />
  </main>
</template>
</template>