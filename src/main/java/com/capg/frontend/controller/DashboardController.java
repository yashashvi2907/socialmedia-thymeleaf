package com.capg.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.capg.frontend.dto.FriendsDTO;
import com.capg.frontend.dto.GroupDTO;
import com.capg.frontend.dto.LikesDTO;
import com.capg.frontend.dto.MessageDTO;
import com.capg.frontend.dto.NotificationDTO;
import com.capg.frontend.dto.PostDto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

@Controller
public class DashboardController {

    @Autowired
    private RestTemplate restTemplate;

    private HttpEntity<String> getAuthorizedEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    private String extractBackendMessage(Exception e) {
        if (e instanceof org.springframework.web.client.HttpClientErrorException clientEx) {
            String body = clientEx.getResponseBodyAsString();
            return parseMessageFromJson(body);
        }

        if (e instanceof org.springframework.web.client.HttpServerErrorException serverEx) {
            String body = serverEx.getResponseBodyAsString();
            return parseMessageFromJson(body);
        }

        return null;
    }

    private String parseMessageFromJson(String body) {
        if (body == null || body.isBlank()) {
            return null;
        }

        int index = body.indexOf("\"message\"");
        if (index == -1) {
            return null;
        }

        int colon = body.indexOf(":", index);
        int firstQuote = body.indexOf("\"", colon + 1);
        int secondQuote = body.indexOf("\"", firstQuote + 1);

        if (colon == -1 || firstQuote == -1 || secondQuote == -1) {
            return null;
        }

        return body.substring(firstQuote + 1, secondQuote);
    }

    @GetMapping("/dashboard/bhavya")
    public String bhavyaDashboard(
            @RequestParam(required = false) Integer userId,
            HttpSession session,
            Model model) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/login";
        }

        if (userId == null) {
            userId = 4;
        }

        HttpEntity<String> entity = getAuthorizedEntity(token);

        try {
            ResponseEntity<PostDto[]> feedResponse =
                    restTemplate.exchange(
                            "http://localhost:8085/api/feed/" + userId,
                            HttpMethod.GET,
                            entity,
                            PostDto[].class
                    );

            ResponseEntity<PostDto[]> trendingResponse =
                    restTemplate.exchange(
                            "http://localhost:8085/api/posts/trending",
                            HttpMethod.GET,
                            entity,
                            PostDto[].class
                    );

            model.addAttribute("feedPosts",
                    feedResponse.getBody() != null ? Arrays.asList(feedResponse.getBody()) : Collections.emptyList());
            model.addAttribute("trendingPosts",
                    trendingResponse.getBody() != null ? Arrays.asList(trendingResponse.getBody()) : Collections.emptyList());
            model.addAttribute("selectedUserId", userId);

        } catch (Exception e) {
            e.printStackTrace();
            String backendMessage = extractBackendMessage(e);
            if (backendMessage != null) {
                model.addAttribute("error", backendMessage);
            }
        }

        return "dashboard_bhavya";
    }

    @GetMapping("/dashboard/mansi")
    public String mansiDashboard(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer userId2,
            @RequestParam(required = false, defaultValue = "pending") String view,
            HttpSession session,
            Model model) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/login";
        }

        model.addAttribute("pendingList", Collections.emptyList());
        model.addAttribute("acceptedList", Collections.emptyList());
        model.addAttribute("mutualList", Collections.emptyList());

        if (userId == null) {
            return "dashboard_mansi";
        }

        HttpEntity<String> entity = getAuthorizedEntity(token);

        try {
            if ("pending".equalsIgnoreCase(view)) {
                FriendsDTO[] pending = restTemplate.exchange(
                        "http://localhost:8085/friends/pending/" + userId,
                        HttpMethod.GET,
                        entity,
                        FriendsDTO[].class
                ).getBody();

                model.addAttribute("pendingList",
                        pending != null ? Arrays.asList(pending) : Collections.emptyList());

            } else if ("accepted".equalsIgnoreCase(view)) {
                FriendsDTO[] accepted = restTemplate.exchange(
                        "http://localhost:8085/friends/accepted/" + userId,
                        HttpMethod.GET,
                        entity,
                        FriendsDTO[].class
                ).getBody();

                model.addAttribute("acceptedList",
                        accepted != null ? Arrays.asList(accepted) : Collections.emptyList());

            } else if ("mutual".equalsIgnoreCase(view)) {
                if (userId2 != null) {
                    FriendsDTO[] mutual = restTemplate.exchange(
                            "http://localhost:8085/friends/mutual?user1=" + userId + "&user2=" + userId2,
                            HttpMethod.GET,
                            entity,
                            FriendsDTO[].class
                    ).getBody();

                    model.addAttribute("mutualList",
                            mutual != null ? Arrays.asList(mutual) : Collections.emptyList());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            String backendMessage = extractBackendMessage(e);
            if (backendMessage != null) {
                model.addAttribute("error", backendMessage);
            }
        }

        return "dashboard_mansi";
    }

    @GetMapping("/dashboard/yashashvi")
    public String yashashviDashboard(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer userId2,
            @RequestParam(required = false, defaultValue = "posts") String view,
            HttpSession session,
            Model model) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/login";
        }

        model.addAttribute("userPosts", Collections.emptyList());
        model.addAttribute("chatList", Collections.emptyList());

        if (userId == null) {
            return "dashboard_yashashvi";
        }

        HttpEntity<String> entity = getAuthorizedEntity(token);

        try {
            if ("posts".equalsIgnoreCase(view)) {
                PostDto[] posts = restTemplate.exchange(
                        "http://localhost:8085/api/posts/user/" + userId,
                        HttpMethod.GET,
                        entity,
                        PostDto[].class
                ).getBody();

                model.addAttribute("userPosts",
                        posts != null ? Arrays.asList(posts) : Collections.emptyList());

            } else if ("chat".equalsIgnoreCase(view)) {
                if (userId2 != null) {
                    MessageDTO[] chats = restTemplate.exchange(
                            "http://localhost:8085/messages/" + userId + "/" + userId2,
                            HttpMethod.GET,
                            entity,
                            MessageDTO[].class
                    ).getBody();

                    model.addAttribute("chatList",
                            chats != null ? Arrays.asList(chats) : Collections.emptyList());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            String backendMessage = extractBackendMessage(e);
            if (backendMessage != null) {
                model.addAttribute("error", backendMessage);
            }
        }

        return "dashboard_yashashvi";
    }

    @GetMapping("/dashboard/disha")
    public String dishaDashboard(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false, defaultValue = "likes") String view,
            HttpSession session,
            Model model) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/login";
        }

        model.addAttribute("likesList", Collections.emptyList());
        model.addAttribute("groupInfo", null);
        model.addAttribute("enteredId", id);

        if (id == null) {
            return "dashboard_disha";
        }

        HttpEntity<String> entity = getAuthorizedEntity(token);

        try {
            if ("likes".equalsIgnoreCase(view)) {
                LikesDTO[] likes = restTemplate.exchange(
                        "http://localhost:8085/api/likes/post/" + id,
                        HttpMethod.GET,
                        entity,
                        LikesDTO[].class
                ).getBody();

                model.addAttribute("likesList",
                        likes != null ? Arrays.asList(likes) : Collections.emptyList());

            } else if ("group".equalsIgnoreCase(view)) {
                GroupDTO group = restTemplate.exchange(
                        "http://localhost:8085/api/groups/" + id,
                        HttpMethod.GET,
                        entity,
                        GroupDTO.class
                ).getBody();

                model.addAttribute("groupInfo", group);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String backendMessage = extractBackendMessage(e);
            if (backendMessage != null) {
                model.addAttribute("error", backendMessage);
            }
        }

        return "dashboard_disha";
    }

    @GetMapping("/dashboard/sakshi")
    public String sakshiDashboard(
            @RequestParam(required = false) String value,
            @RequestParam(required = false, defaultValue = "notifications") String view,
            HttpSession session,
            Model model) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/login";
        }

        model.addAttribute("notificationList", Collections.emptyList());
        model.addAttribute("searchPosts", Collections.emptyList());
        model.addAttribute("enteredValue", value);

        if (value == null || value.trim().isEmpty()) {
            return "dashboard_sakshi";
        }

        HttpEntity<String> entity = getAuthorizedEntity(token);

        try {
            if ("notifications".equalsIgnoreCase(view)) {
                NotificationDTO[] notifications = restTemplate.exchange(
                        "http://localhost:8085/api/notifications/" + value,
                        HttpMethod.GET,
                        entity,
                        NotificationDTO[].class
                ).getBody();

                model.addAttribute("notificationList",
                        notifications != null ? Arrays.asList(notifications) : Collections.emptyList());

            } else if ("search".equalsIgnoreCase(view)) {
                PostDto[] posts = restTemplate.exchange(
                        "http://localhost:8085/api/posts/search?keyword=" +
                                java.net.URLEncoder.encode(value, StandardCharsets.UTF_8),
                        HttpMethod.GET,
                        entity,
                        PostDto[].class
                ).getBody();

                model.addAttribute("searchPosts",
                        posts != null ? Arrays.asList(posts) : Collections.emptyList());
            }

        } catch (Exception e) {
            e.printStackTrace();
            String backendMessage = extractBackendMessage(e);
            if (backendMessage != null) {
                model.addAttribute("error", backendMessage);
            }
        }

        return "dashboard_sakshi";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}