package com.aistory.dto;

import java.util.UUID;

public class StartStoryRequest {
    private String initialPrompt;
    private UUID aiConfigId;

    // 构造函数
    public StartStoryRequest() {}

    // Getters and Setters
    public String getInitialPrompt() {
        return initialPrompt;
    }

    public void setInitialPrompt(String initialPrompt) {
        this.initialPrompt = initialPrompt;
    }

    public UUID getAiConfigId() {
        return aiConfigId;
    }

    public void setAiConfigId(UUID aiConfigId) {
        this.aiConfigId = aiConfigId;
    }
}