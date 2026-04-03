package com.capg.frontend.dto;

public class GroupDTO {

    private Integer groupID;
    private String groupName;

    private Integer adminID;
    private String adminUsername;
    private String adminEmail;

    public GroupDTO(Integer groupID, String groupName,
                    Integer adminID, String adminUsername, String adminEmail) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.adminID = adminID;
        this.adminUsername = adminUsername;
        this.adminEmail = adminEmail;
    }

    public Integer getGroupID() { return groupID; }
    public String getGroupName() { return groupName; }
    public Integer getAdminID() { return adminID; }
    public String getAdminUsername() { return adminUsername; }
    public String getAdminEmail() { return adminEmail; }
}






























