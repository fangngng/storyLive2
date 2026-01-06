package com.aistory.dto;

import java.util.UUID;

public class StoryOptionDto {
    private UUID id;
    private String text;
    private UUID nextNodeId;

    // 构造函数
    public StoryOptionDto() {}

    public StoryOptionDto(UUID id, String text, UUID nextNodeId) {
        this.id = id;
        this.text = text;
        this.nextNodeId = nextNodeId;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getNextNodeId() {
        return nextNodeId;
    }

    public void setNextNodeId(UUID nextNodeId) {
        this.nextNodeId = nextNodeId;
    }
}