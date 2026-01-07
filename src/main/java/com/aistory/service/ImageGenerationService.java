package com.aistory.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ImageGenerationService {
    
    @Value("${openai.api.key:}")
    private String openaiApiKey;
    
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public String generateImage(String prompt) {
        // 如果没有API密钥，返回占位符图片URL
        if (openaiApiKey == null || openaiApiKey.isEmpty()) {
            return "https://placehold.co/800x600?text=" + 
                   java.net.URLEncoder.encode(prompt, java.nio.charset.StandardCharsets.UTF_8);
        }
        
        try {
            // 创建请求体
            String requestBody = String.format(
                "{\"model\":\"dall-e-2\",\"prompt\":\"%s\",\"n\":1,\"size\":\"800x600\"}", 
                prompt.replace("\"", "\\\"")
            );
            
            // 创建HTTP请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/images/generations"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + openaiApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .timeout(Duration.ofSeconds(60))
                    .build();
            
            // 发送请求
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            // 解析响应
            if (response.statusCode() == 200) {
                JsonNode responseJson = objectMapper.readTree(response.body());
                JsonNode dataNode = responseJson.get("data");
                
                if (dataNode != null && dataNode.isArray() && dataNode.size() > 0) {
                    JsonNode firstImage = dataNode.get(0);
                    if (firstImage.has("url")) {
                        return firstImage.get("url").asText();
                    }
                }
            } else {
                System.err.println("API请求失败，状态码: " + response.statusCode());
                System.err.println("响应体: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("调用OpenAI图像API失败: " + e.getMessage());
            Thread.currentThread().interrupt(); // 重要：恢复中断状态
        } catch (Exception e) {
            System.err.println("调用OpenAI图像API失败: " + e.getMessage());
        }
        
        // 如果API调用失败，返回占位符图片
        return "https://placehold.co/800x600?text=" + 
               java.net.URLEncoder.encode(prompt, java.nio.charset.StandardCharsets.UTF_8);
    }
}