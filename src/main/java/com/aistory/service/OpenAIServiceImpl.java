package com.aistory.service;

import com.aistory.model.AIConfiguration;
import com.aistory.model.StoryNode;
import com.aistory.model.StoryOption;
import com.aistory.repository.StoryOptionRepository;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class OpenAIServiceImpl implements AIService {
    
    @Value("${openai.api.key:}")
    private String openaiApiKey;
    
    @Autowired
    private StoryOptionRepository storyOptionRepository;
    
    private OpenAiApi openAiApi;
    
    public OpenAIServiceImpl() {
        // 初始化将在需要时完成
    }

    @Override
    public StoryNode generateStoryContent(String prompt, String context, UUID sessionId, AIConfiguration config) {
        // 检查API密钥
        if (openaiApiKey == null || openaiApiKey.isEmpty()) {
            log.warn("OpenAI API密钥未配置，使用模拟实现");
            return generateMockStoryContent(prompt, context, sessionId);
        }
        
        // 初始化OpenAI API客户端（如果尚未初始化）
        if (openAiApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(
                    new okhttp3.OkHttpClient.Builder()
                        .connectTimeout(Duration.ofSeconds(60))
                        .readTimeout(Duration.ofSeconds(60))
                        .writeTimeout(Duration.ofSeconds(60))
                        .build()
                )
                .build();
            
            openAiApi = retrofit.create(OpenAiApi.class);
        }
        
        StoryNode node = new StoryNode();
        node.setSessionId(sessionId);
        node.setNodeType(StoryNode.NodeType.CONTINUATION);
        
        try {
            // 构建提示词
            String fullPrompt = buildFullPrompt(prompt, context);
            
            // 创建聊天请求
            List<ChatMessage> messages = new ArrayList<>();
            ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "你是一个专业的故事创作助手，专门创作互动式故事。请创作一段连贯的故事内容，并在结尾提供3个不同的选项供读者选择。格式为：\n故事内容...\n\n选项：\n1. 选项一\n2. 选项二\n3. 选项三");
            ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), fullPrompt);
            
            messages.add(systemMessage);
            messages.add(userMessage);
            
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(config.getModelName() != null ? config.getModelName() : "gpt-3.5-turbo")
                    .messages(messages)
                    .temperature(config.getTemperature() != null ? config.getTemperature() : 0.7)
                    .maxTokens(config.getMaxTokens() != null ? config.getMaxTokens() : 500)
                    .topP(config.getTopP() != null ? config.getTopP() : 1.0)
                    .build();
            
            // 使用同步方式调用API
            retrofit2.Response<com.theokanning.openai.completion.chat.ChatCompletionResult> response = 
                openAiApi.createChatCompletion(request).execute();
            
            if (response.isSuccessful() && response.body() != null && 
                response.body().getChoices() != null && !response.body().getChoices().isEmpty()) {
                
                String content = response.body().getChoices().get(0).getMessage().getContent();
                
                // 解析内容和选项
                ParseResult parseResult = parseContentAndOptions(content);
                node.setContent(parseResult.content);
                
                // 生成图片提示词
                String imagePrompt = generateImagePrompt(parseResult.content);
                node.setImagePrompt(imagePrompt);
                
                return node;
            } else {
                log.warn("OpenAI API返回错误: {}", response.code());
                log.warn("响应体: {}", response.errorBody() != null ? response.errorBody().string() : "null");
                return generateMockStoryContent(prompt, context, sessionId);
            }
            
        } catch (Exception e) {
            log.error("调用OpenAI API失败: {}", e.getMessage(), e);
            // 如果API调用失败，返回模拟内容
            return generateMockStoryContent(prompt, context, sessionId);
        }
    }
    
    private String buildFullPrompt(String prompt, String context) {
        if (context == null || context.isEmpty()) {
            return "请开始一个关于'" + prompt + "'的有趣故事，内容连贯且引人入胜。请在故事结尾提供3个不同的选项供读者选择，格式为：\n故事内容...\n\n选项：\n1. 选项一\n2. 选项二\n3. 选项三";
        } else {
            return "基于以下上下文继续故事：'" + context + "'，并根据用户的选择'" + prompt + "'继续发展情节。请保持故事的连贯性和逻辑性。请在故事结尾提供3个不同的选项供读者选择，格式为：\n故事内容...\n\n选项：\n1. 选项一\n2. 选项二\n3. 选项三";
        }
    }
    
    private ParseResult parseContentAndOptions(String content) {
        // 查找选项部分
        Pattern optionPattern = Pattern.compile("(?s)(.*?)\\n\\s*选项：\\s*\\n(.*)", Pattern.DOTALL);
        Matcher matcher = optionPattern.matcher(content);
        
        if (matcher.find()) {
            String storyContent = matcher.group(1).trim();
            String optionsText = matcher.group(2).trim();
            
            // 解析选项
            String[] optionLines = optionsText.split("\\n");
            List<String> options = new ArrayList<>();
            for (String line : optionLines) {
                line = line.trim();
                if (line.startsWith("1.") || line.startsWith("2.") || line.startsWith("3.") ||
                    line.startsWith("4.") || line.startsWith("5.")) {
                    // 提取选项文本（去掉编号）
                    String optionText = line.replaceFirst("^\\d+\\.", "").trim();
                    if (!optionText.isEmpty()) {
                        options.add(optionText);
                    }
                }
            }
            
            return new ParseResult(storyContent, options);
        } else {
            // 如果没有找到选项格式，返回全部内容作为故事内容
            return new ParseResult(content, new ArrayList<>());
        }
    }
    
    private String generateImagePrompt(String content) {
        // 根据故事内容生成图片提示词
        return "Fantasy scene: " + content.substring(0, Math.min(content.length(), 50)) + " background";
    }
    
    private StoryNode generateMockStoryContent(String prompt, String context, UUID sessionId) {
        StoryNode node = new StoryNode();
        node.setSessionId(sessionId);
        node.setNodeType(StoryNode.NodeType.CONTINUATION);
        
        // 这里是模拟实现，在实际应用中应该调用OpenAI API
        String content;
        if (context == null || context.isEmpty()) {
            content = "在一个遥远的王国里，" + prompt + "。这是一个神奇的开始，充满了无限的可能性。冒险者们面临着一个重要的选择：是探索神秘的森林，还是前往古老的城堡？";
        } else {
            content = context + " " + prompt + "继续展开，情节变得更加复杂和引人入胜。";
        }
        
        node.setContent(content);
        
        // 生成图片提示词
        String imagePrompt = generateImagePrompt(content);
        node.setImagePrompt(imagePrompt);
        
        return node;
    }
    
    // 内部类用于解析内容和选项
    private static class ParseResult {
        public final String content;
        public final List<String> options;
        
        public ParseResult(String content, List<String> options) {
            this.content = content;
            this.options = options;
        }
    }
}