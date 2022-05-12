package com.jam.service;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the emailing service of
 * the reimbursement API.
 */
@Slf4j
@Service
@ToString
public class EmailService {
    @Value("${email-api.url:none}")
    private String url;

    /**
     * Sends an HTTP request to the email API
     * with the contents of the email.
     * @param recipient The recipient of the email.
     * @param subject The subject of the email.
     * @param body The body contents of the email.
     */
    public void sendEmail(String recipient, String subject, String body) {
        Map<String, Object> map = new HashMap<>();
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        map.put("recipient", recipient);
        map.put("subject", subject);
        map.put("message", body);
        log.info("[POST] Email request received to be sent to " + url);
        if (url != null) {
            try {
                restTemplate.postForEntity(url, map, ResponseEntity.class);
                log.info("[POST] Email request sent out to email API " + url);
                log.info("[POST] Email recipient: " + recipient);
                log.info("[POST] Email subject: " + subject);
                log.info("[POST] Email body: " + body);
            } catch (Exception e) {
                log.info("[POST] Failed to send out email due to error: " + e.getMessage());
            }
        } else {
            log.info("[POST] Email was not sent because API url doesn't exist.");
        }
    }
}
