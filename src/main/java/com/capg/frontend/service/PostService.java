package com.capg.frontend.service;

import com.capg.frontend.dto.PostDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class PostService {

    @Value("${backend.base-url}")
    private String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public List<PostDto> getTrending(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<PostDto[]> response = restTemplate.exchange(
                baseUrl + "/api/posts/trending",
                HttpMethod.GET,
                entity,
                PostDto[].class
        );

        return Arrays.asList(response.getBody());
    }
}