package com.aistory.service;

import com.aistory.model.AIConfiguration;
import com.aistory.model.StoryNode;
import com.aistory.model.StoryOption;
import com.aistory.repository.StoryOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OpenAIServiceImpl implements AIService {
    
    @Value("${openai.api.key:}")
    private String openaiApiKey;
    
    @Autowired
    private StoryOptionRepository storyOptionRepository;
    
    @Override
    public StoryNode generateStoryContent(String prompt, String context, UUID sessionId, AIConfiguration config) {
        StoryNode node = new StoryNode();
        node.setSessionId(sessionId);
        node.setNodeType(StoryNode.NodeType.CONTINUATION);
        
        // 在实际实现中，这里会调用OpenAI API
        // 为了演示，我们使用模拟数据
        String mockContent = generateMockStoryContent(prompt, context);
        node.setContent(mockContent);
        
        // 生成图片提示词
        String imagePrompt = generateImagePrompt(mockContent);
        node.setImagePrompt(imagePrompt);
        
        // 生成模拟选项
        List<StoryOption> options = generateMockOptions(node.getId());
        // 由于选项需要先保存节点才能设置nextNodeId，我们稍后处理
        
        return node;
    }
    
    private String generateMockStoryContent(String prompt, String context) {
        // 这里是模拟实现，在实际应用中应该调用OpenAI API
        if (context == null || context.isEmpty()) {
            return "在一个遥远的王国里，" + prompt + "。这是一个神奇的开始，充满了无限的可能性。冒险者们面临着一个重要的选择：是探索神秘的森林，还是前往古老的城堡？";
        } else {
            return context + " " + prompt + "继续展开，情节变得更加复杂和引人入胜。";
        }
    }
    
    private String generateImagePrompt(String content) {
        // 根据故事内容生成图片提示词
        return "Fantasy scene: " + content.substring(0, Math.min(content.length(), 50)) + " background";
    }
    
    private List<StoryOption> generateMockOptions(UUID nodeId) {
        List<StoryOption> options = new ArrayList<>();
        
        StoryOption option1 = new StoryOption();
        option1.setNodeId(nodeId);
        option1.setOptionText("探索神秘的森林");
        option1.setNextNodeId(UUID.randomUUID());
        options.add(option1);
        
        StoryOption option2 = new StoryOption();
        option2.setNodeId(nodeId);
        option2.setOptionText("前往古老的城堡");
        option2.setNextNodeId(UUID.randomUUID());
        options.add(option2);
        
        StoryOption option3 = new StoryOption();
        option3.setNodeId(nodeId);
        option3.setOptionText("寻找智慧的长者");
        option3.setNextNodeId(UUID.randomUUID());
        options.add(option3);
        
        return options;
    }
}