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

import java.util.Arrays;

@Controller
public class DashboardController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/dashboard/bhavya")
    public String bhavyaDashboard(
            @RequestParam(required = false) Integer userId,
            HttpSession session,
            Model model) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/login";
        }

        // 👉 Default userId (only for first load)
        if (userId == null) {
            userId = 4; // just default view (NOT mapping)
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // 🔥 FEED API (dynamic)
            ResponseEntity<PostDto[]> feedResponse =
                    restTemplate.exchange(
                            "http://localhost:8085/api/feed/" + userId,
                            HttpMethod.GET,
                            entity,
                            PostDto[].class
                    );

            // 🔥 TRENDING API
            ResponseEntity<PostDto[]> trendingResponse =
                    restTemplate.exchange(
                            "http://localhost:8085/api/posts/trending",
                            HttpMethod.GET,
                            entity,
                            PostDto[].class
                    );

            model.addAttribute("feedPosts", Arrays.asList(feedResponse.getBody()));
            model.addAttribute("trendingPosts", Arrays.asList(trendingResponse.getBody()));
            model.addAttribute("selectedUserId", userId);

        } catch (Exception e) {
            model.addAttribute("error", "Error fetching data");
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

        model.addAttribute("pendingList", java.util.Collections.emptyList());
        model.addAttribute("acceptedList", java.util.Collections.emptyList());
        model.addAttribute("mutualList", java.util.Collections.emptyList());

        if (userId == null) {
            return "dashboard_mansi";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            if ("pending".equals(view)) {
                FriendsDTO[] pending = restTemplate.exchange(
                        "http://localhost:8085/friends/pending/" + userId,
                        HttpMethod.GET,
                        entity,
                        FriendsDTO[].class
                ).getBody();

                model.addAttribute("pendingList",
                        pending != null ? Arrays.asList(pending) : java.util.Collections.emptyList());

            } else if ("accepted".equals(view)) {
                FriendsDTO[] accepted = restTemplate.exchange(
                        "http://localhost:8085/friends/accepted/" + userId,
                        HttpMethod.GET,
                        entity,
                        FriendsDTO[].class
                ).getBody();

                model.addAttribute("acceptedList",
                        accepted != null ? Arrays.asList(accepted) : java.util.Collections.emptyList());

            } else if ("mutual".equals(view)) {
                if (userId2 != null) {
                    FriendsDTO[] mutual = restTemplate.exchange(
                            "http://localhost:8085/friends/mutual/" + userId + "/" + userId2,
                            HttpMethod.GET,
                            entity,
                            FriendsDTO[].class
                    ).getBody();

                    model.addAttribute("mutualList",
                            mutual != null ? Arrays.asList(mutual) : java.util.Collections.emptyList());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error fetching data from backend");
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

        model.addAttribute("userPosts", java.util.Collections.emptyList());
        model.addAttribute("chatList", java.util.Collections.emptyList());

        if (userId == null) {
            return "dashboard_yashashvi";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            if ("posts".equals(view)) {
                PostDto[] posts = restTemplate.exchange(
                        "http://localhost:8085/api/posts/user/" + userId,
                        HttpMethod.GET,
                        entity,
                        PostDto[].class
                ).getBody();

                model.addAttribute("userPosts",
                        posts != null ? Arrays.asList(posts) : java.util.Collections.emptyList());

            } else if ("chat".equals(view)) {
                if (userId2 != null) {
                    MessageDTO[] chats = restTemplate.exchange(
                            "http://localhost:8085/messages/" + userId + "/" + userId2,
                            HttpMethod.GET,
                            entity,
                            MessageDTO[].class
                    ).getBody();

                    model.addAttribute("chatList",
                            chats != null ? Arrays.asList(chats) : java.util.Collections.emptyList());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error fetching data from backend");
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

        model.addAttribute("likesList", java.util.Collections.emptyList());
        model.addAttribute("groupInfo", null);
        model.addAttribute("enteredId", id);

        if (id == null) {
            return "dashboard_disha";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            if ("likes".equals(view)) {
                LikesDTO[] likes = restTemplate.exchange(
                        "http://localhost:8085/api/likes/post/" + id,
                        HttpMethod.GET,
                        entity,
                        LikesDTO[].class
                ).getBody();

                model.addAttribute("likesList",
                        likes != null ? Arrays.asList(likes) : java.util.Collections.emptyList());

            } else if ("group".equals(view)) {
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
            model.addAttribute("error", "Error fetching data from backend");
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

        model.addAttribute("notificationList", java.util.Collections.emptyList());
        model.addAttribute("searchPosts", java.util.Collections.emptyList());
        model.addAttribute("enteredValue", value);

        if (value == null || value.trim().isEmpty()) {
            return "dashboard_sakshi";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            if ("notifications".equals(view)) {
                NotificationDTO[] notifications = restTemplate.exchange(
                        "http://localhost:8085/api/notifications/" + value,
                        HttpMethod.GET,
                        entity,
                        NotificationDTO[].class
                ).getBody();

                model.addAttribute("notificationList",
                        notifications != null ? Arrays.asList(notifications) : java.util.Collections.emptyList());

            } else if ("search".equals(view)) {
                PostDto[] posts = restTemplate.exchange(
                        "http://localhost:8085/api/posts/search?keyword=" + java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8),
                        HttpMethod.GET,
                        entity,
                        PostDto[].class
                ).getBody();

                model.addAttribute("searchPosts",
                        posts != null ? Arrays.asList(posts) : java.util.Collections.emptyList());
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error fetching data from backend");
        }

        return "dashboard_sakshi";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}