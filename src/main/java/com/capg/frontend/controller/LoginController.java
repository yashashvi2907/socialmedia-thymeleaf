package com.capg.frontend.controller;

import com.capg.frontend.dto.LoginDTO;
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

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        try {
            String url = "http://localhost:8085/account/login";

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(username);
            loginDTO.setPassword(password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<LoginDTO> entity = new HttpEntity<>(loginDTO, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, entity, String.class);

            String token = response.getBody();

            if (token == null || token.isBlank()) {
                model.addAttribute("error", "Token not received from backend");
                return "login";
            }

            token = token.trim();

            if (token.startsWith("\"") && token.endsWith("\"")) {
                token = token.substring(1, token.length() - 1);
            }

            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            session.setAttribute("token", token);
            session.setAttribute("username", username);

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
            e.printStackTrace();
            model.addAttribute("error", "Invalid login");
            return "login";
        }
    }
}