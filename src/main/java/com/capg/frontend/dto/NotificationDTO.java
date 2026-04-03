package com.capg.frontend.dto;

import java.time.LocalDateTime;
import java.util.Base64;

public class NotificationDTO {

    private String content;
    private LocalDateTime timestamp;
    private String username;
    private String email;
    private byte[] profilePicture;

    public NotificationDTO(){}

    public NotificationDTO(String content, LocalDateTime timestamp,
                           String username, String email, byte[] profilePicture) {
        this.content = content;
        this.timestamp = timestamp;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
    }


    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
//    public byte[] getProfilePicture() { return profilePicture; }
public String getProfilePicture() {
    if (profilePicture == null || profilePicture.length == 0) {
        return null;
    }
    return Base64.getEncoder().encodeToString(profilePicture);
}
}