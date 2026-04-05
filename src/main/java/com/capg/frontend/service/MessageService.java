package com.capg.frontend.service;

import com.capg.frontend.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {

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

    public List<MessageDTO> getChatsBetweenUsers(Integer userId1, Integer userId2, String token) {

        String url = baseUrl + "/messages/" + userId1 + "/" + userId2;

        ResponseEntity<MessageDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getAuthorizedEntity(token),
                MessageDTO[].class
        );

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }
}