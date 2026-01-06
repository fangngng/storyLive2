package com.aistory.service;

import com.aistory.model.AIConfiguration;
import com.aistory.model.StoryNode;

import java.util.UUID;

public interface AIService {
    StoryNode generateStoryContent(String prompt, String context, UUID sessionId, AIConfiguration config);
}