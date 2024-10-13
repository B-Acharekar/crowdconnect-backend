package com.crowdconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.crowdconnect.dto.SolutionDto;
import com.crowdconnect.model.Solution;
import com.crowdconnect.model.User;
import com.crowdconnect.model.Vote;
import com.crowdconnect.model.VoteType;
import com.crowdconnect.repository.SolutionRepository;
import com.crowdconnect.repository.UserRepository;
import com.crowdconnect.repository.VoteRepository;


@Service
public class VoteService {

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    // Vote on a solution
    public void voteOnSolution(Long solutionId, String username, VoteType voteType) {
        Solution solution = solutionRepository.findById(solutionId)
            .orElseThrow(() -> new RuntimeException("Solution not found"));

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Vote existingVote = voteRepository.findBySolutionAndUser(solution, user);

        if (existingVote != null) {
            // If the user already voted, update the vote type
            existingVote.setVoteType(voteType);
            voteRepository.save(existingVote);
        } else {
            // Create a new vote
            Vote vote = new Vote();
            vote.setSolution(solution);
            vote.setUser(user);
            vote.setVoteType(voteType);
            voteRepository.save(vote);
        }
    }

    // Get a solution with its vote counts and user vote
    public SolutionDto getSolutionWithVotes(Long solutionId) {
        Solution solution = solutionRepository.findById(solutionId)
            .orElseThrow(() -> new RuntimeException("Solution not found"));

        SolutionDto dto = mapToDto(solution);
        dto.setUpvoteCount(voteRepository.countBySolutionAndVoteType(solution, VoteType.UPVOTE));
        dto.setDownvoteCount(voteRepository.countBySolutionAndVoteType(solution, VoteType.DOWNVOTE));

        // Get the current user from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            Vote userVote = voteRepository.findBySolutionAndUser(solution, currentUser);
            if (userVote != null) {
                dto.setUserVote(userVote.getVoteType());
            }
        }

        return dto;
    }

 // Helper method to map Solution to SolutionDto
    private SolutionDto mapToDto(Solution solution) {
        SolutionDto dto = new SolutionDto();
        dto.setId(solution.getId());
        dto.setDescription(solution.getDescription());
        dto.setUsername(solution.getCreatedBy().getUsername());
        dto.setProblemId(solution.getProblem().getId());
        dto.setProblemTitle(solution.getProblem().getTitle()); // Set the problem title
        dto.setCreatedAt(solution.getCreatedAt()); // Set createdAt timestamp
        dto.setUpdatedAt(solution.getUpdatedAt()); // Set updatedAt timestamp
        // Note: The upvoteCount and downvoteCount fields should be set in the getSolutionWithVotes method
        return dto;
    }

}
