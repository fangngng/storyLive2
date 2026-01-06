package com.aistory.dto;

import java.util.List;
import java.util.UUID;

public class StoryNodeResponse {
    private UUID id;
    private UUID sessionId;
    private String content;
    private String imageUrl;
    private List<StoryOptionDto> options;
    private java.time.LocalDateTime createdAt;

    // 构造函数
    public StoryNodeResponse() {}

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<StoryOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<StoryOptionDto> options) {
        this.options = options;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}