package com.capg.frontend.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    // show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // handle login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        try {
            String url = "http://localhost:8085/account/login";

            // create JSON body
            String requestBody = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, entity, String.class);

            String token = response.getBody();

            // store token in session
            session.setAttribute("token", token);
            session.setAttribute("username", username);

            // 🔥 REDIRECT BASED ON USER
            switch (username) {
                case "Bhavya":
                    return "redirect:/dashboard/bhavya";
                case "Mansi Roy":
                    return "redirect:/dashboard/mansi";
                case "Yashashvi":
                    return "redirect:/dashboard/yashashvi";
                case "Disha":
                    return "redirect:/dashboard/disha";
                case "Sakshi":
                    return "redirect:/dashboard/sakshi";
                default:
                    return "redirect:/login";
            }

        } catch (Exception e) {
            e.printStackTrace(); // IMPORTANT
            model.addAttribute("error", "Invalid login");
            return "login";
        }
    }
}