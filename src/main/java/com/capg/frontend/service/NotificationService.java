package com.capg.frontend.service;

import com.capg.frontend.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationService {

    @Value("${backend.base-url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    private HttpEntity<Void> getAuthorizedEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    public List<NotificationDTO> getNotificationsByUser(String userId, String token) {

        String url = baseUrl + "/api/notifications/" + userId;

        ResponseEntity<NotificationDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getAuthorizedEntity(token),
                NotificationDTO[].class
        );

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }
}