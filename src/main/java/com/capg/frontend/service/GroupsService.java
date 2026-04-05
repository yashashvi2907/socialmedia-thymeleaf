package com.capg.frontend.service;

import com.capg.frontend.dto.GroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GroupsService {

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

    public GroupDTO getGroupById(Integer groupId, String token) {

        String url = baseUrl + "/api/groups/" + groupId;

        ResponseEntity<GroupDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getAuthorizedEntity(token),
                GroupDTO.class
        );

        return response.getBody();
    }
}