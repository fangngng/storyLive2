package com.aistory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "story_options")
public class StoryOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "node_id", nullable = false)
    private UUID nodeId;  // 关联到StoryNode的ID
    
    @Lob
    @Column(name = "option_text", nullable = false)
    private String optionText;
    
    @Column(name = "next_node_id", nullable = false)
    private UUID nextNodeId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public StoryOption() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getNodeId() {
        return nodeId;
    }

    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public UUID getNextNodeId() {
        return nextNodeId;
    }

    public void setNextNodeId(UUID nextNodeId) {
        this.nextNodeId = nextNodeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}