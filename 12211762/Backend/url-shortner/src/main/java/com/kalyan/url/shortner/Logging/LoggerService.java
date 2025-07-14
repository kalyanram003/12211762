package com.kalyan.url.shortner.Logging;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoggerService {

    private static final String LOGGING_API = "http://20.244.56.144/evaluation-service/logs";
    private final RestTemplate restTemplate = new RestTemplate();

    public void log(String stack, String level, String packageName, String message) {
        LogRequest log = new LogRequest(stack, level, packageName, message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LogRequest> req = new HttpEntity<>(log, headers);

        try {
            restTemplate.postForEntity(LOGGING_API, req, String.class);
        } catch (Exception e) {
            System.out.println("Logging failed: " + e.getMessage());
        }
    }
}
