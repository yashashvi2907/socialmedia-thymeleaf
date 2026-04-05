package com.capg.frontend.service;

import com.capg.frontend.dto.LikesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LikesService {

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

    public List<LikesDTO> getLikesByPost(Integer postId, String token) {

        String url = baseUrl + "/api/likes/post/" + postId;

        ResponseEntity<LikesDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getAuthorizedEntity(token),
                LikesDTO[].class
        );

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }
}