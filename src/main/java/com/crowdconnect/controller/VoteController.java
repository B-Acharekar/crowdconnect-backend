package com.crowdconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.crowdconnect.dto.SolutionDto;
import com.crowdconnect.model.VoteType;
import com.crowdconnect.service.VoteService;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "http://localhost:3000")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/solution/{solutionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> voteOnSolution(@PathVariable("solutionId") Long solutionId,
                                                @RequestParam("voteType") VoteType voteType) {
        // Get the current user's authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Retrieves the username directly

        // Call the service method to record the vote
        voteService.voteOnSolution(solutionId, username, voteType);
        return ResponseEntity.ok().build(); // Return 200 OK with no content
    }

    @GetMapping("/solution/{solutionId}")
    public ResponseEntity<SolutionDto> getSolutionWithVotes(@PathVariable Long solutionId) {
        SolutionDto solutionDto = voteService.getSolutionWithVotes(solutionId);
        return ResponseEntity.ok(solutionDto);
    }

}
