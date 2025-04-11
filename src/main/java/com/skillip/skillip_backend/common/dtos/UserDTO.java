package com.skillip.skillip_backend.common.dtos;

public class UserDTO {
    private String username;
    private String email;
    // as profile images are stored in S3 bucket, we have to generate a pre-signed URL to access the image
    // and we have to pass the complete URL to the client
    private String profileImageUrl;
    // as presigned URL is generated for certain time, we have to pass the expiry time to the client
    private String imageUrlExpiration;

    public UserDTO(String username, String email, String profileImageUrl, String imageUrlExpiration) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.imageUrlExpiration = imageUrlExpiration;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getImageUrlExpiration() {
        return imageUrlExpiration;
    }

    public void setImageUrlExpiration(String imageUrlExpiration) {
        this.imageUrlExpiration = imageUrlExpiration;
    }
    
}
