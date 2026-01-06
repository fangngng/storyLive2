package com.aistory.dto;

import java.util.UUID;

public class ContinueStoryRequest {
    private UUID sessionId;
    private UUID optionId;

    // 构造函数
    public ContinueStoryRequest() {}

    // Getters and Setters
    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getOptionId() {
        return optionId;
    }

    public void setOptionId(UUID optionId) {
        this.optionId = optionId;
    }
}