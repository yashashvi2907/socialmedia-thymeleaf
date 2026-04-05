package com.capg.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.capg.frontend.dto.GroupDTO;
import com.capg.frontend.service.FeedService;
import com.capg.frontend.service.FriendsService;
import com.capg.frontend.service.GroupService;
import com.capg.frontend.service.LikesService;
import com.capg.frontend.service.MessageService;
import com.capg.frontend.service.NotificationService;
import com.capg.frontend.service.PostService;
import com.capg.frontend.service.UserService;

import java.util.Collections;

@Controller
public class DashboardController {

    @Autowired
    private FeedService feedService;

    @Autowired
    private PostService postService;

    @Autowired
    private FriendsService friendsService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showDashboard(Model model) {
        model.addAttribute("theme", "light");
        return "dashboard";
    }

    @GetMapping("/dashboard/bhavya")
    public String bhavyaDashboard(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false, defaultValue = "feed") String tab,
            HttpSession session,
            Model model) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/login";
        }

        model.addAttribute("selectedUserId", userId);
        model.addAttribute("activeTab", tab);
        model.addAttribute("feedPosts", Collections.emptyList());
        model.addAttribute("trendingPosts", Collections.emptyList());
        model.addAttribute("feedError", null);
        model.addAttribute("trendingError", null);

        if (userId != null) {
            try {
                model.addAttribute("feedPosts", feedService.getFeed(userId, token));
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("feedError", getBackendErrorMessage(e, "Unable to load feed"));
            }
        }

        try {
            model.addAttribute("trendingPosts", postService.getTrending(token));
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("trendingError", getBackendErrorMessage(e, "Unable to load trending posts"));
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

        model.addAttribute("activeView", view);
        model.addAttribute("selectedUserId", userId);
        model.addAttribute("selectedUserId2", userId2);

        model.addAttribute("pendingList", Collections.emptyList());
        model.addAttribute("acceptedList", Collections.emptyList());
        model.addAttribute("mutualList", Collections.emptyList());

        model.addAttribute("pendingError", null);
        model.addAttribute("acceptedError", null);
        model.addAttribute("mutualError", null);

        if ("pending".equalsIgnoreCase(view) && userId != null) {
            try {
                model.addAttribute("pendingList", friendsService.getPendingFriends(userId, token));
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("pendingError", getBackendErrorMessage(e, "Unable to load pending requests"));
            }
        } else if ("accepted".equalsIgnoreCase(view) && userId != null) {
            try {
                model.addAttribute("acceptedList", friendsService.getAcceptedFriends(userId, token));
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("acceptedError", getBackendErrorMessage(e, "Unable to load accepted friends"));
            }
        } else if ("mutual".equalsIgnoreCase(view) && userId != null && userId2 != null) {
            try {
                model.addAttribute("mutualList", friendsService.getMutualFriends(userId, userId2, token));
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("mutualError", getBackendErrorMessage(e, "Unable to load mutual friends"));
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

        model.addAttribute("activeView", view);
        model.addAttribute("selectedUserId", userId);
        model.addAttribute("selectedUserId2", userId2);

        model.addAttribute("userPosts", Collections.emptyList());
        model.addAttribute("chatList", Collections.emptyList());

        model.addAttribute("postsError", null);
        model.addAttribute("chatError", null);

        if ("posts".equalsIgnoreCase(view) && userId != null) {
            try {
                model.addAttribute("userPosts", userService.getUserPosts(userId, token));
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("postsError", getBackendErrorMessage(e, "Unable to load posts"));
            }
        } else if ("chat".equalsIgnoreCase(view) && userId != null && userId2 != null) {
            try {
                model.addAttribute("chatList", messageService.getChatsBetweenUsers(userId, userId2, token));
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("chatError", getBackendErrorMessage(e, "Unable to load chat"));
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

        model.addAttribute("activeView", view);
        model.addAttribute("enteredId", id);

        model.addAttribute("likesList", Collections.emptyList());
        model.addAttribute("groupInfo", null);

        model.addAttribute("likesError", null);
        model.addAttribute("groupError", null);

        if ("likes".equalsIgnoreCase(view) && id != null) {
            try {
                model.addAttribute("likesList", likesService.getLikesByPost(id, token));
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("likesError", getBackendErrorMessage(e, "Unable to load likes"));
            }
        } else if ("group".equalsIgnoreCase(view) && id != null) {
            try {
                GroupDTO group = groupService.getGroupById(id, token);
                model.addAttribute("groupInfo", group);
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("groupError", getBackendErrorMessage(e, "Unable to load group info"));
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

        model.addAttribute("activeView", view);
        model.addAttribute("enteredValue", value);

        model.addAttribute("notificationList", Collections.emptyList());
        model.addAttribute("searchPosts", Collections.emptyList());

        model.addAttribute("notificationError", null);
        model.addAttribute("searchError", null);

        if ("notifications".equalsIgnoreCase(view) && value != null && !value.trim().isEmpty()) {
            try {
                model.addAttribute("notificationList", notificationService.getNotificationsByUser(value, token));
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("notificationError", getBackendErrorMessage(e, "Unable to load notifications"));
            }
        } else if ("search".equalsIgnoreCase(view) && value != null && !value.trim().isEmpty()) {
            try {
                model.addAttribute("searchPosts", postService.searchPosts(value, token));
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("searchError", getBackendErrorMessage(e, "Unable to search posts"));
            }
        }

        return "dashboard_sakshi";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private String getBackendErrorMessage(Exception e, String defaultMessage) {

        if (e instanceof org.springframework.web.client.HttpClientErrorException ex) {
            String body = ex.getResponseBodyAsString();
            String message = extractMessage(body);
            return message != null ? message : defaultMessage;
        }

        if (e instanceof org.springframework.web.client.HttpServerErrorException ex) {
            String body = ex.getResponseBodyAsString();
            String message = extractMessage(body);
            return message != null ? message : defaultMessage;
        }

        return defaultMessage;
    }

    private String extractMessage(String body) {
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
}