package com.godev.nailManagement.infra.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService implements AIServiceInterface {

    @Value("${GOOGLE_API_KEY}")
    private String apiKey;

    @Value("${GEMINI_MODEL}")
    private String model;

    private final Client client;
    private final GenerateContentConfig config;

    @Override
    public String rewriteMessage(String baseMessage) {

        String prompt = """
                Reescreva o texto abaixo de forma amigável, natural e profissional.
                Não adicione informações novas.
                Não altere o sentido original.
                Use português do Brasil.
                
                Texto:
                """ + baseMessage;

        try {
            GenerateContentResponse response = client.models.generateContent(
                    model,
                    prompt,
                    config
            );

            return response.text();

        } catch (Exception e) {
            log.warn("Error on Gemini calling, returning base message! {}",  e.getMessage());
            return baseMessage;
        }
    }


}
