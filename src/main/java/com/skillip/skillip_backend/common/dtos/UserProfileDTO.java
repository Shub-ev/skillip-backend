package com.skillip.skillip_backend.common.dtos;

import java.time.LocalDateTime;

public class UserProfileDTO
{
    private String phoneNumber;
    private LocalDateTime createdAt;
    private Boolean isTalent;
    private String profileBannerUrl;
    private String[] links;

    public UserProfileDTO() {
    }

    public UserProfileDTO(LocalDateTime createdAt, Boolean isTalent, String[] links, String phoneNumber, String profileBannerUrl) {
        this.createdAt = createdAt;
        this.isTalent = isTalent;
        this.links = links;
        this.phoneNumber = phoneNumber;
        this.profileBannerUrl = profileBannerUrl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserProfileDTO{");
        sb.append("phoneNumber=").append(phoneNumber);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", isTalent=").append(isTalent);
        sb.append(", profileBannerUrl=").append(profileBannerUrl);
        sb.append(", links=").append(links);
        sb.append('}');
        return sb.toString();
    }



    /** Setters */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setIsTalent(Boolean isTalent) {
        this.isTalent = isTalent;
    }

    public void setProfileBannerUrl(String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }

    public void setLinks(String[] links) {
        this.links = links;
    }


    /** Getters */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getIsTalent() {
        return isTalent;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public String[] getLinks() {
        return links;
    }


}