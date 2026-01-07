package com.aistory.controller;

import com.aistory.dto.*;
import com.aistory.model.StorySession;
import com.aistory.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/story")
@CrossOrigin(origins = "*") // 为开发方便，允许所有来源，生产环境应配置具体域名
public class StoryController {
    
    @Autowired
    private StoryService storyService;
    
    @PostMapping("/start")
    public ResponseEntity<StoryNodeResponse> startNewStory(@RequestBody StartStoryRequest request) {
        StoryNodeResponse response = storyService.startNewStory(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/continue")
    public ResponseEntity<StoryNodeResponse> continueStory(@RequestBody ContinueStoryRequest request) {
        StoryNodeResponse response = storyService.continueStory(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<StoryNodeResponse>> getStoryHistory(@PathVariable UUID sessionId) {
        List<StoryNodeResponse> responses = storyService.getSessionHistory(sessionId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/sessions")
    public ResponseEntity<List<StorySession>> getAllSessions() {
        List<StorySession> sessions = storyService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }
    
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteStorySession(@PathVariable UUID sessionId) {
        // 在实际实现中，这里应该实现删除会话的逻辑
        // 目前我们只是返回成功状态
        return ResponseEntity.ok().build();
    }
}