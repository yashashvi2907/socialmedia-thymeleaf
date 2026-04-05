package com.capg.frontend.service;

import com.capg.frontend.dto.FriendsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FriendsService {

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

    public List<FriendsDTO> getPendingFriends(Integer userId, String token) {

        ResponseEntity<FriendsDTO[]> response = restTemplate.exchange(
                baseUrl + "/friends/pending/" + userId,
                HttpMethod.GET,
                getAuthorizedEntity(token),
                FriendsDTO[].class
        );

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }

    public List<FriendsDTO> getAcceptedFriends(Integer userId, String token) {

        ResponseEntity<FriendsDTO[]> response = restTemplate.exchange(
                baseUrl + "/friends/accepted/" + userId,
                HttpMethod.GET,
                getAuthorizedEntity(token),
                FriendsDTO[].class
        );

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }

    public List<FriendsDTO> getMutualFriends(Integer userId1, Integer userId2, String token) {

        String url = baseUrl + "/friends/mutual?user1=" + userId1 + "&user2=" + userId2;

        ResponseEntity<FriendsDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getAuthorizedEntity(token),
                FriendsDTO[].class
        );

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }
}