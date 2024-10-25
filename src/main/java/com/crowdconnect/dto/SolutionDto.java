package com.crowdconnect.dto;

import java.time.LocalDateTime;

import com.crowdconnect.model.SolutionStatus;
import com.crowdconnect.model.VoteType;

public class SolutionDto {
    private Long id;
    private String description;
    private String username;
    private Long problemId;
    private String problemTitle;  // Problem title field
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long upvoteCount;
    private Long downvoteCount;
    private VoteType userVote;  // Track the user's vote
    private SolutionStatus status;       // Add status field

    // Constructors
    public SolutionDto() {}

    public SolutionDto(String description, String username) {
        this.description = description;
        this.username = username;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(Long upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public Long getDownvoteCount() {
        return downvoteCount;
    }

    public void setDownvoteCount(Long downvoteCount) {
        this.downvoteCount = downvoteCount;
    }

    public VoteType getUserVote() {
        return userVote;
    }

    public void setUserVote(VoteType userVote) {
        this.userVote = userVote;
    }

    public SolutionStatus getStatus() {
        return status;  // Getter for status
    }

    public void setStatus(SolutionStatus status) {
        this.status = status; // Setter for status
    }
}
