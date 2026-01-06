package com.aistory.controller;

import com.aistory.dto.AIConfigurationResponse;
import com.aistory.model.AIConfiguration;
import com.aistory.repository.AIConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*")
public class AIConfigController {
    
    @Autowired
    private AIConfigurationRepository aiConfigurationRepository;
    
    @GetMapping("/ai")
    public ResponseEntity<List<AIConfigurationResponse>> getAllAIConfigurations() {
        List<AIConfiguration> configs = aiConfigurationRepository.findAll();
        List<AIConfigurationResponse> responses = configs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/ai/default")
    public ResponseEntity<AIConfigurationResponse> getDefaultAIConfiguration() {
        Optional<AIConfiguration> config = aiConfigurationRepository.findByIsDefaultTrue();
        if (config.isPresent()) {
            return ResponseEntity.ok(convertToResponse(config.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/ai/{id}")
    public ResponseEntity<AIConfigurationResponse> updateAIConfiguration(
            @PathVariable UUID id,
            @RequestBody AIConfiguration config) {
        Optional<AIConfiguration> existingConfig = aiConfigurationRepository.findById(id);
        if (existingConfig.isPresent()) {
            AIConfiguration updatedConfig = existingConfig.get();
            updatedConfig.setModelName(config.getModelName());
            updatedConfig.setTemperature(config.getTemperature());
            updatedConfig.setMaxTokens(config.getMaxTokens());
            updatedConfig.setTopP(config.getTopP());
            updatedConfig.setIsDefault(config.getIsDefault());
            
            // 如果设置了新的默认配置，取消其他配置的默认状态
            if (Boolean.TRUE.equals(config.getIsDefault())) {
                List<AIConfiguration> allConfigs = aiConfigurationRepository.findAll();
                for (AIConfiguration c : allConfigs) {
                    if (!c.getId().equals(id) && Boolean.TRUE.equals(c.getIsDefault())) {
                        c.setIsDefault(false);
                        aiConfigurationRepository.save(c);
                    }
                }
            }
            
            AIConfiguration savedConfig = aiConfigurationRepository.save(updatedConfig);
            return ResponseEntity.ok(convertToResponse(savedConfig));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private AIConfigurationResponse convertToResponse(AIConfiguration config) {
        AIConfigurationResponse response = new AIConfigurationResponse();
        response.setId(config.getId());
        response.setModelName(config.getModelName());
        response.setTemperature(config.getTemperature());
        response.setMaxTokens(config.getMaxTokens());
        response.setTopP(config.getTopP());
        response.setIsDefault(config.getIsDefault());
        return response;
    }
}