package com.aistory.service;

import com.aistory.dto.*;
import com.aistory.model.*;
import com.aistory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoryService {
    
    @Autowired
    private StoryNodeRepository storyNodeRepository;
    
    @Autowired
    private StoryOptionRepository storyOptionRepository;
    
    @Autowired
    private StorySessionRepository storySessionRepository;
    
    @Autowired
    private AIConfigurationRepository aiConfigurationRepository;
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private ImageGenerationService imageGenerationService;
    
    @Transactional
    public StoryNodeResponse startNewStory(StartStoryRequest request) {
        // 创建新的会话
        StorySession session = new StorySession();
        session.setUserId(null); // 暂时设为null，后续可扩展用户系统
        session.setTitle("新故事会话");
        session.setStatus(StorySession.SessionStatus.ACTIVE);
        
        StorySession savedSession = storySessionRepository.save(session);
        
        // 获取AI配置，如果没有指定则使用默认配置
        AIConfiguration aiConfig = null;
        if (request.getAiConfigId() != null) {
            aiConfig = aiConfigurationRepository.findById(request.getAiConfigId()).orElse(null);
        }
        
        if (aiConfig == null) {
            aiConfig = aiConfigurationRepository.findByIsDefaultTrue().orElse(null);
        }
        
        if (aiConfig == null) {
            // 如果没有默认配置，则创建一个
            aiConfig = new AIConfiguration();
            aiConfig.setModelName("gpt-3.5-turbo");
            aiConfig.setIsDefault(true);
            aiConfig = aiConfigurationRepository.save(aiConfig);
        }
        
        // 生成故事内容
        String initialPrompt = request.getInitialPrompt() != null ? request.getInitialPrompt() : "开始一个有趣的故事";
        StoryNode aiGeneratedNode = aiService.generateStoryContent(initialPrompt, null, savedSession.getId(), aiConfig);
        
        // 生成背景图片
        String imageUrl = imageGenerationService.generateImage(aiGeneratedNode.getImagePrompt());
        aiGeneratedNode.setImageUrl(imageUrl);
        
        // 保存故事节点
        StoryNode savedNode = storyNodeRepository.save(aiGeneratedNode);
        
        // 转换为响应对象
        return convertToResponse(savedNode);
    }
    
    @Transactional
    public StoryNodeResponse continueStory(ContinueStoryRequest request) {
        // 获取选择的选项
        StoryOption selectedOption = storyOptionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new RuntimeException("选项不存在"));
        
        // 获取当前会话
        StorySession session = storySessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("会话不存在"));
        
        // 获取当前故事节点以提供上下文
        List<StoryNode> sessionNodes = storyNodeRepository.findBySessionId(session.getId());
        String context = buildContext(sessionNodes);
        
        // 使用AI生成下一个故事节点
        AIConfiguration aiConfig = aiConfigurationRepository.findByIsDefaultTrue()
                .orElseThrow(() -> new RuntimeException("未找到默认AI配置"));
        
        StoryNode aiGeneratedNode = aiService.generateStoryContent(selectedOption.getOptionText(), context, session.getId(), aiConfig);
        
        // 生成背景图片
        String imageUrl = imageGenerationService.generateImage(aiGeneratedNode.getImagePrompt());
        aiGeneratedNode.setImageUrl(imageUrl);
        
        // 保存故事节点
        StoryNode savedNode = storyNodeRepository.save(aiGeneratedNode);
        
        // 转换为响应对象
        return convertToResponse(savedNode);
    }
    
    public List<StoryNodeResponse> getStoryHistory(UUID sessionId) {
        List<StoryNode> nodes = storyNodeRepository.findBySessionId(sessionId);
        return nodes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    private String buildContext(List<StoryNode> nodes) {
        StringBuilder context = new StringBuilder();
        for (StoryNode node : nodes) {
            context.append(node.getContent()).append(" ");
        }
        return context.toString();
    }
    
    private StoryNodeResponse convertToResponse(StoryNode node) {
        StoryNodeResponse response = new StoryNodeResponse();
        response.setId(node.getId());
        response.setSessionId(node.getSessionId());
        response.setContent(node.getContent());
        response.setImageUrl(node.getImageUrl());
        response.setCreatedAt(node.getCreatedAt());
        
        // 获取与该节点相关的选项
        List<StoryOption> options = storyOptionRepository.findByNodeId(node.getId());
        List<StoryOptionDto> optionDtos = options.stream()
                .map(option -> new StoryOptionDto(option.getId(), option.getOptionText(), option.getNextNodeId()))
                .collect(Collectors.toList());
        
        response.setOptions(optionDtos);
        
        return response;
    }
}