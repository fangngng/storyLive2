package com.aistory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "story_nodes")
public class StoryNode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "session_id", nullable = false)
    private UUID sessionId;
    
    @Lob
    @Column(name = "content", nullable = false)
    private String content;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Lob
    @Column(name = "image_prompt")
    private String imagePrompt;
    
    @Column(name = "node_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NodeType nodeType;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "nodeId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryOption> options;
    
    public enum NodeType {
        START, CONTINUATION, OPTION, END
    }

    // 构造函数
    public StoryNode() {
        this.createdAt = LocalDateTime.now();
    }

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

    public String getImagePrompt() {
        return imagePrompt;
    }

    public void setImagePrompt(String imagePrompt) {
        this.imagePrompt = imagePrompt;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<StoryOption> getOptions() {
        return options;
    }

    public void setOptions(List<StoryOption> options) {
        this.options = options;
    }
}