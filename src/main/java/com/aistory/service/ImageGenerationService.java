package com.aistory.service;

import org.springframework.stereotype.Service;

@Service
public class ImageGenerationService {
    
    public String generateImage(String prompt) {
        // 在实际实现中，这里会调用图像生成API（如DALL-E）
        // 为了演示，返回一个模拟的图片URL
        return "https://placehold.co/800x600?text=" + 
               java.net.URLEncoder.encode(prompt, java.nio.charset.StandardCharsets.UTF_8);
    }
}