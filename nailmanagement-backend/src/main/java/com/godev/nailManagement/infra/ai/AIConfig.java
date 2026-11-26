package com.godev.nailManagement.infra.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public Client geminiClient(
            @Value("${GOOGLE_API_KEY}") String apiKey
    ) {
        try {
            return Client.builder()
                    .apiKey(apiKey)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Bean
    public GenerateContentConfig generateContentConfig() {
        return GenerateContentConfig.builder()
                .temperature(0.5f)
                .topP(0.9f)
                .maxOutputTokens(150)
                .build();
    }
}
