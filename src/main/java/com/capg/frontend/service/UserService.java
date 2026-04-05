package com.capg.frontend.service;

import com.capg.frontend.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

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

    public List<PostDTO> getUserPosts(Integer userId, String token) {

        String url = baseUrl + "/api/posts/user/" + userId;

        ResponseEntity<PostDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getAuthorizedEntity(token),
                PostDTO[].class
        );

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }
}