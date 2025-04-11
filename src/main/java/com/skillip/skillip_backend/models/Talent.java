package com.skillip.skillip_backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "talents")
public class Talent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String skills;

    @Column(length = 255)
    private String portfolioURL;

    @Column(length = 255)
    private String resumeURL;

    @Column(length = 255)
    private String linkedinURL;

    @Column(length = 255)
    private String githubURL;

    @Column(length = 1000)
    private String workExp;

    @Column(length = 500)
    private String talentImageURL;


    public Talent(){}
    public Talent(Long id, User user, String skills, String portfolioURL, String resumeURL, String linkedinURL, String githubURL, String workExp, String talentImageURL) {
        this.id = id;
        this.user = user;
        this.skills = skills;
        this.portfolioURL = portfolioURL;
        this.resumeURL = resumeURL;
        this.linkedinURL = linkedinURL;
        this.githubURL = githubURL;
        this.workExp = workExp;
        this.talentImageURL = talentImageURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getPortfolioURL() {
        return portfolioURL;
    }

    public void setPortfolioURL(String portfolioURL) {
        this.portfolioURL = portfolioURL;
    }

    public String getResumeURL() {
        return resumeURL;
    }

    public void setResumeURL(String resumeURL) {
        this.resumeURL = resumeURL;
    }

    public String getLinkedinURL() {
        return linkedinURL;
    }

    public void setLinkedinURL(String linkedinURL) {
        this.linkedinURL = linkedinURL;
    }

    public String getGithubURL() {
        return githubURL;
    }

    public void setGithubURL(String githubURL) {
        this.githubURL = githubURL;
    }

    public String getWorkExp() {
        return workExp;
    }

    public void setWorkExp(String workExp) {
        this.workExp = workExp;
    }

    public String getTalentImageURL() {
        return talentImageURL;
    }

    public void setTalentImageURL(String talentImageURL) {
        this.talentImageURL = talentImageURL;
    }
}
