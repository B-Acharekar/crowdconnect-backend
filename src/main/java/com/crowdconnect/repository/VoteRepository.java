package com.crowdconnect.repository;

import com.crowdconnect.model.Solution;
import com.crowdconnect.model.User;
import com.crowdconnect.model.Vote;
import com.crowdconnect.model.VoteType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Long countBySolutionAndVoteType(Solution solution, VoteType voteType);
    Vote findBySolutionAndUser(Solution solution, User user);
}
