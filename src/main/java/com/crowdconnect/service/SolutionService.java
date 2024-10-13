package com.crowdconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.crowdconnect.dto.SolutionDto;
import com.crowdconnect.model.Problem;
import com.crowdconnect.model.Solution;
import com.crowdconnect.model.User;
import com.crowdconnect.model.Vote;
import com.crowdconnect.model.VoteType;
import com.crowdconnect.repository.ProblemRepository;
import com.crowdconnect.repository.SolutionRepository;
import com.crowdconnect.repository.UserRepository;
import com.crowdconnect.repository.VoteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolutionService {

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private VoteRepository voteRepository;

    // Add solution to a problem (return SolutionDto)
    public SolutionDto addSolution(Long problemId, String description, String username) {
        Problem problem = problemRepository.findById(problemId)
            .orElseThrow(() -> new RuntimeException("Problem not found"));

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Solution solution = new Solution();
        solution.setDescription(description);
        solution.setProblem(problem);
        solution.setCreatedBy(user);

        // Increment user's contribution count
        user.setContributionCount(user.getContributionCount() + 1);
        userRepository.save(user);

        Solution savedSolution = solutionRepository.save(solution);

        // Return the SolutionDto
        return mapToDto(savedSolution);
    }

    // Get solutions for a problem (return List<SolutionDto>)
    public List<SolutionDto> getSolutions(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
            .orElseThrow(() -> new RuntimeException("Problem not found"));

        List<Solution> solutions = solutionRepository.findByProblem(problem);
        
        // Convert List<Solution> to List<SolutionDto>
        return solutions.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    public List<SolutionDto> getAllSolutions() {
        List<Solution> solutions = solutionRepository.findAll();
        return solutions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    // Update a solution by ID
    public SolutionDto updateSolution(Long solutionId, String newDescription) {
        Solution solution = solutionRepository.findById(solutionId)
            .orElseThrow(() -> new RuntimeException("Solution not found"));

        // Update the solution's description
        solution.setDescription(newDescription);

        Solution updatedSolution = solutionRepository.save(solution);

        return mapToDto(updatedSolution); // Convert the updated entity to DTO and return
    }

    // Delete a solution by ID
    public void deleteSolution(Long solutionId) {
        Solution solution = solutionRepository.findById(solutionId)
            .orElseThrow(() -> new RuntimeException("Solution not found"));

        // Delete the solution
        solutionRepository.delete(solution);
    }

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
        dto.setUpvoteCount(voteRepository.countBySolutionAndVoteType(solution, VoteType.UPVOTE)); // Set upvote count
        dto.setDownvoteCount(voteRepository.countBySolutionAndVoteType(solution, VoteType.DOWNVOTE)); // Set downvote count
        return dto;
    }


}
