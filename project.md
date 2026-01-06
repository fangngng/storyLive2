# AI 故事生成系统项目文档

## 1. 项目概述

### 1.1 项目名称
AI 故事生成系统（StoryLive2）

### 1.2 项目描述
一个基于AI的互动故事生成平台，用户可以通过选择不同的故事选项来体验个性化的故事内容。系统会根据故事内容动态生成相应的背景图片，为用户提供沉浸式的阅读体验。

### 1.3 项目愿景
创建一个能够让用户沉浸在个性化AI生成故事中的平台，通过互动选项引导故事发展，并提供视觉上吸引人的背景图片，增强故事的沉浸感。

## 2. 功能特性

### 2.1 核心功能
- ✨ **打字机效果**: 故事内容以打字机效果逐字显示，增强阅读体验
- 🧠 **AI故事生成**: 使用AI模型生成连贯的故事内容和可选分支
- 🖼️ **动态图片生成**: 根据故事内容生成匹配的背景图片
- 📚 **历史记录**: 保存用户的故事历史记录
- ⚙️ **AI模型管理**: 支持AI模型和参数的配置管理
- 📱 **响应式设计**: 支持桌面和移动设备的自适应布局

### 2.2 附加功能
- 用户账户系统（可选）
- 故事收藏功能
- 分享功能
- 多语言支持（可选）

## 3. 技术架构

### 3.1 整体架构
采用前后端分离的微服务架构：
```
┌─────────────────┐    HTTP/REST    ┌──────────────────┐
│   前端应用      │ ──────────────→  │   后端服务       │
│               │                 │                  │
│ Vue 3 + Vite  │ ←────────────── │ Spring Boot API  │
│ Vue Router    │   WebSocket     │ SQLite Database  │
│ Pinia         │                 │ AI Integration   │
│ Tailwind CSS  │                 │ Image Service    │
└─────────────────┘               └──────────────────┘
```

### 3.2 前端技术栈
- **框架**: Vue 3 + Vite
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP客户端**: Axios
- **样式**: Tailwind CSS
- **构建工具**: Vite
- **UI组件**: 原生组件 + 可能的UI库

### 3.3 后端技术栈
- **框架**: Spring Boot 3.x
- **编程语言**: Java 17+
- **数据库**: SQLite
- **Web框架**: Spring Web MVC
- **安全**: Spring Security（可选）
- **数据访问**: Spring Data JPA

### 3.4 AI集成
- **文本生成**: OpenAI GPT 或其他AI语言模型
- **图片生成**: DALL-E 或其他AI图像生成服务

## 4. 数据库设计

### 4.1 数据库选择
使用 SQLite 作为数据库，具有轻量级、易于部署和维护的特点。

### 4.2 数据表结构

#### 4.2.1 用户表 (users)
```
- id: UUID (主键)
- username: VARCHAR(50) (用户名)
- email: VARCHAR(100) (邮箱)
- created_at: TIMESTAMP (创建时间)
- updated_at: TIMESTAMP (更新时间)
```

#### 4.2.2 故事节点表 (story_nodes)
```
- id: UUID (主键)
- session_id: UUID (会话ID)
- content: TEXT (故事内容)
- image_url: VARCHAR(500) (背景图片URL)
- image_prompt: TEXT (图片生成提示词)
- node_type: VARCHAR(20) (节点类型: start, continuation, option, end)
- created_at: TIMESTAMP (创建时间)
```

#### 4.2.3 故事选项表 (story_options)
```
- id: UUID (主键)
- node_id: UUID (关联的故事节点ID)
- option_text: TEXT (选项文本)
- next_node_id: UUID (下一个节点ID)
- created_at: TIMESTAMP (创建时间)
```

#### 4.2.4 会话历史表 (story_sessions)
```
- id: UUID (主键)
- user_id: UUID (用户ID)
- title: VARCHAR(200) (故事标题)
- started_at: TIMESTAMP (开始时间)
- ended_at: TIMESTAMP (结束时间)
- status: VARCHAR(20) (状态: active, completed, abandoned)
```

#### 4.2.5 AI配置表 (ai_configurations)
```
- id: UUID (主键)
- model_name: VARCHAR(100) (模型名称)
- temperature: DECIMAL(2,1) (温度参数)
- max_tokens: INTEGER (最大token数)
- top_p: DECIMAL(2,1) (top_p参数)
- is_default: BOOLEAN (是否为默认配置)
- created_at: TIMESTAMP (创建时间)
```

## 5. API 接口设计

### 5.1 前端API接口

#### 5.1.1 故事相关接口
```
POST /api/story/start - 开始新故事
POST /api/story/continue - 继续故事
POST /api/story/option - 选择故事选项
GET /api/story/history - 获取历史记录
DELETE /api/story/{id} - 删除历史记录
```

#### 5.1.2 配置相关接口
```
GET /api/config/ai - 获取AI配置
PUT /api/config/ai - 更新AI配置
GET /api/config/ai/default - 获取默认AI配置
```

### 5.2 数据传输对象 (DTO)

#### 5.2.1 请求对象
```java
// StartStoryRequest
{
  "initialPrompt": "string",
  "aiConfigId": "string"
}

// ContinueStoryRequest
{
  "sessionId": "string",
  "optionId": "string"
}

// UpdateAIConfigRequest
{
  "id": "string",
  "modelName": "string",
  "temperature": 0.7,
  "maxTokens": 500,
  "topP": 0.9
}
```

#### 5.2.2 响应对象
```java
// StoryNodeResponse
{
  "id": "string",
  "sessionId": "string",
  "content": "string",
  "imageUrl": "string",
  "options": [
    {
      "id": "string",
      "text": "string",
      "nextNodeId": "string"
    }
  ],
  "createdAt": "datetime"
}

// AIConfigResponse
{
  "id": "string",
  "modelName": "string",
  "temperature": 0.7,
  "maxTokens": 500,
  "topP": 0.9,
  "isDefault": true
}
```

## 6. 前端页面结构

### 6.1 页面路由
```
/ - 首页/故事开始页面
/story/:id - 故事阅读页面
/history - 历史记录页面
/settings - 设置页面
```

### 6.2 主要组件

#### 6.2.1 核心组件
- **StoryDisplay**: 故事内容展示组件（带打字机效果）
- **StoryOption**: 故事选项选择组件
- **BackgroundImage**: 背景图片展示组件
- **TypewriterEffect**: 打字机效果封装组件
- **LoadingSpinner**: 加载指示器组件

#### 6.2.2 页面组件
- **HomeView**: 首页/开始故事页面
- **StoryView**: 故事阅读页面
- **HistoryView**: 历史记录页面
- **SettingsView**: 设置页面

### 6.3 状态管理
使用 Pinia 进行全局状态管理：
- **storyStore**: 管理当前故事状态
- **historyStore**: 管理历史记录
- **settingsStore**: 管理设置和AI配置

## 7. AI集成方案

### 7.1 文本生成
集成 OpenAI GPT 模型或其他语言模型来生成故事内容：
- 根据当前故事上下文生成后续内容
- 生成多个故事选项供用户选择
- 确保故事的连贯性和逻辑性

### 7.2 图片生成
集成 DALL-E 或其他图像生成服务：
- 根据故事内容生成背景图片的提示词
- 调用图像生成API创建匹配的背景图片
- 优化提示词以获得最佳视觉效果

### 7.3 AI配置管理
允许用户调整AI模型参数：
- Temperature (控制创造性)
- Max Tokens (控制输出长度)
- Top P (控制输出多样性)
- Model Selection (选择不同的AI模型)

## 8. 用户界面设计

### 8.1 设计原则
- 简洁直观的用户界面
- 沉浸式的阅读体验
- 响应式设计，适配各种设备
- 流畅的动画和过渡效果

### 8.2 视觉元素
- 舒适的阅读字体和行距
- 柔和的色彩方案，不干扰阅读
- 动态背景图片增强氛围
- 清晰的选项按钮设计

### 8.3 交互设计
- 打字机效果增强阅读体验
- 平滑的页面过渡动画
- 直观的选项选择流程
- 便捷的历史记录访问

## 9. 开发计划

### 9.1 第一阶段 - 基础架构 (Week 1-2)
- 搭建前后端基础架构
- 配置数据库和基本API
- 实现基本的用户界面框架

### 9.2 第二阶段 - 核心功能 (Week 3-4)
- 实现故事生成和选项选择功能
- 集成AI文本生成服务
- 实现打字机效果

### 9.3 第三阶段 - 图像功能 (Week 5)
- 集成图像生成服务
- 实现动态背景图片功能

### 9.4 第四阶段 - 增强功能 (Week 6)
- 实现历史记录功能
- 添加设置和AI配置管理
- 优化用户界面和体验

### 9.5 第五阶段 - 测试和部署 (Week 7)
- 进行全面测试
- 性能优化
- 部署和发布

## 10. 部署方案

### 10.1 后端部署
- 使用 JAR 包部署
- 支持 Docker 容器化部署
- 配置环境变量管理敏感信息

### 10.2 前端部署
- 构建静态文件
- 部署到 Web 服务器或 CDN
- 支持 SPA 路由的服务器配置

### 10.3 数据库部署
- SQLite 数据库文件部署
- 数据备份和恢复策略
- 数据迁移脚本

## 11. 安全考虑

### 11.1 API 安全
- 实现适当的认证和授权机制
- 防止滥用 AI 服务的速率限制
- 输入验证和清理

### 11.2 数据安全
- 敏感信息加密存储
- 安全的会话管理
- 数据传输加密

## 12. 性能优化

### 12.1 前端优化
- 组件懒加载
- 图片优化和懒加载
- 代码分割和压缩

### 12.2 后端优化
- API 响应缓存
- 数据库查询优化
- AI 服务调用优化

## 13. 扩展性考虑

### 13.1 功能扩展
- 支持多种AI模型
- 多语言支持
- 社交功能集成

### 13.2 技术扩展
- 微服务架构演进
- 消息队列支持
- 实时协作功能