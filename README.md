# AI 故事生成系统

一个基于AI的互动故事生成平台，用户可以通过选择不同的故事选项来体验个性化的故事内容。系统会根据故事内容动态生成相应的背景图片，为用户提供沉浸式的阅读体验。

## 功能特性

- ✨ 打字机效果展示故事内容
- 🧠 AI模型生成连贯的故事内容和选项
- 🖼️ 根据故事内容生成匹配的背景图片
- 📚 历史记录功能
- ⚙️ AI模型管理和参数配置
- 📱 响应式设计，支持移动设备

## 技术栈

### 前端
- Vue 3 + Vite
- Vue Router
- Pinia 状态管理
- Axios HTTP客户端
- Tailwind CSS 样式设计

### 后端
- Spring Boot 3.x
- Spring AI (集成OpenAI)
- Java 17+
- SQLite 数据库
- JPA 数据访问

### AI服务
- OpenAI GPT 模型 (用于故事生成)
- OpenAI DALL-E 模型 (用于图像生成)

## 项目结构

```
AI 故事生成系统
├── src/main/java/com/aistory/    # 后端源代码
│   ├── config/                  # 配置类
│   ├── controller/              # API控制器
│   ├── dto/                     # 数据传输对象
│   ├── model/                   # 实体模型
│   ├── repository/              # 数据访问层
│   └── service/                 # 业务逻辑层
├── frontend/                    # 前端源代码
│   ├── src/                     # 源代码
│   ├── public/                  # 静态资源
│   └── package.json            # 前端依赖配置
├── pom.xml                     # Maven配置文件
├── mvnw*                       # Maven Wrapper
├── application.yml             # 应用配置
└── README.md                   # 项目说明
```

## 快速开始

### 环境要求

- Java 17 或更高版本
- Node.js 16 或更高版本
- npm (Node.js 包管理器)
- OpenAI API 密钥

### 安装和运行

1. **克隆项目**
   ```bash
   git clone <项目地址>
   cd story-live2
   ```

2. **配置API密钥**
   设置环境变量：
   ```bash
   export OPENAI_API_KEY="your-openai-api-key-here"
   ```

3. **启动后端服务**
   ```bash
   # Windows
   ./mvnw.cmd spring-boot:run
   
   # Linux/Mac
   ./mvnw spring-boot:run
   ```

4. **启动前端服务**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

5. **访问应用**
   - 前端: `http://localhost:5173`
   - 后端API: `http://localhost:8080/api/`

## API 端点

- `POST /api/story/start` - 开始新故事
- `POST /api/story/continue` - 继续故事
- `GET /api/story/history/{sessionId}` - 获取故事历史
- `GET /api/config/ai` - 获取AI配置
- `PUT /api/config/ai/{id}` - 更新AI配置

## 数据库设计

使用 SQLite 作为数据库，包含以下表：

- `story_sessions` - 故事会话
- `story_nodes` - 故事节点
- `story_options` - 故事选项
- `ai_configurations` - AI配置

## 项目文档

- [启动指南](start.md) - 详细说明如何安装和运行项目
- [开发文档](DEVELOPMENT.md) - 项目架构和开发说明
- [项目规划](project.md) - 项目详细规划和设计
- [项目总结](PROJECT_SUMMARY.md) - 项目完成情况总结

## 技术实现

### AI 集成
- 使用 Spring AI 集成 OpenAI 服务
- 支持 GPT 模型生成故事内容
- 支持 DALL-E 模型生成背景图片

### 前端特性
- Vue 3 Composition API
- TypeScript 类型安全
- Pinia 状态管理
- 响应式设计
- 打字机效果

### 后端特性
- Spring Boot 3.x
- Spring Data JPA
- RESTful API 设计
- 依赖注入
- 面向接口编程

## 扩展功能

- 用户账户系统（可选）
- 故事收藏功能
- 分享功能
- 多语言支持（可选）

## 许可证

此项目仅供学习和参考使用。