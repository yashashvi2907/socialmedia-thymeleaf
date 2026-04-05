package com.capg.frontend.controller;

import com.capg.frontend.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private AuthService authService;

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
            String token = authService.login(username, password);

            if (token == null || token.isBlank()) {
                model.addAttribute("error", "Token not received from backend");
                return "login";
            }

            token = token.trim();

            if (token.startsWith("\"") && token.endsWith("\"")) {
                token = token.substring(1, token.length() - 1);
            }

            // keep Bearer token as it is
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