package com.capg.frontend.dto;

import java.time.LocalDateTime;

public class PostDto {

    private Integer postID;
    private String content;
    private LocalDateTime timestamp;

    private Integer userID;
    private String username;

    private int likeCount;
    private int commentCount;

    //Constructors
    public PostDto() {
    }

    public PostDto(Integer postID, String content, LocalDateTime timestamp,
                   Integer userID, String username,
                   int likeCount, int commentCount) {
        this.postID = postID;
        this.content = content;
        this.timestamp = timestamp;
        this.userID = userID;
        this.username = username;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    // 🔹 Getters and Setters

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}