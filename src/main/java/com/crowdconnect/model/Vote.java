package com.crowdconnect.model;

import jakarta.persistence.*;

@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solution_id", nullable = false)
    private Solution solution;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }
}
