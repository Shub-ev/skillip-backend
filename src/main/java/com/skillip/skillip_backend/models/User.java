package com.skillip.skillip_backend.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true, length = 60)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String profileImageUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isTalent = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Talent talent;

    @Column(length = 255)
    private String profileBannerUrl;

    @ElementCollection
    @CollectionTable(name = "user_links", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "link")
    private List<String> links;

    public User() {
    }

    public User(Long id, String username, String email, String password, String phoneNumber,
                String profileImageUrl, Boolean isTalent, Talent talent,
                String profileBannerUrl, List<String> links) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.isTalent = isTalent;
        this.talent = talent;
        this.profileBannerUrl = profileBannerUrl;
        this.links = links;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getIsTalent() {
        return isTalent;
    }

    public Talent getTalent() {
        return talent;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setIsTalent(Boolean isTalent) {
        this.isTalent = isTalent;
    }

    public void setTalent(Talent talent) {
        this.talent = talent;
        if (talent != null) {
            talent.setUser(this);
        }
    }

    public void setProfileBannerUrl(String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", profileImageUrl='").append(profileImageUrl).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", isTalent=").append(isTalent);
        if (profileBannerUrl != null) {
            sb.append(", profileBannerUrl='").append(profileBannerUrl).append('\'');
        }
        if (links != null && !links.isEmpty()) {
            sb.append(", links=").append(String.join(", ", links));
        }
        sb.append('}');
        return sb.toString();
    }
}
