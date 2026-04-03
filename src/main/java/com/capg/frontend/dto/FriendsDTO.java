package com.capg.frontend.dto;

public class FriendsDTO {

    private Integer friendshipID;
    private Integer userID1;
    private Integer userID2;
    private String status;

    public FriendsDTO() {}

    public FriendsDTO(Integer friendshipID, Integer userID1, Integer userID2, String status) {
        this.friendshipID = friendshipID;
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.status = status;
    }

    public Integer getFriendshipID() { return friendshipID; }
    public void setFriendshipID(Integer friendshipID) { this.friendshipID = friendshipID; }

    public Integer getUserID1() { return userID1; }
    public void setUserID1(Integer userID1) { this.userID1 = userID1; }

    public Integer getUserID2() { return userID2; }
    public void setUserID2(Integer userID2) { this.userID2 = userID2; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}