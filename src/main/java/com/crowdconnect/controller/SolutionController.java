package com.crowdconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.crowdconnect.dto.SolutionDto;
import com.crowdconnect.service.SolutionService;

import java.util.List;

@RestController
@RequestMapping("/api/solutions")
@CrossOrigin(origins = "http://localhost:3000")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;
    
    // Post a solution
    @PostMapping("/problem/{problemId}")
    @PreAuthorize("isAuthenticated()") // Ensure user is logged in
    public ResponseEntity<SolutionDto> submitSolution(@PathVariable("problemId") Long problemId, @RequestBody SolutionDto solutionDto) {
        SolutionDto solution = solutionService.addSolution(problemId, solutionDto.getDescription(), solutionDto.getUsername(),solutionDto.getStatus());
        return ResponseEntity.ok(solution);
    }

    // Get solutions for a problem
    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<SolutionDto>> getSolutions(@PathVariable("problemId") Long problemId) {
        List<SolutionDto> solutionDtos = solutionService.getSolutions(problemId);
        return ResponseEntity.ok(solutionDtos);
    }
    
    @GetMapping("/solutions")
    public ResponseEntity<List<SolutionDto>> getAllSolutions() {
        List<SolutionDto> solutionDtos = solutionService.getAllSolutions();
        return ResponseEntity.ok(solutionDtos);
    }
    
    
 // Endpoint to update the description of a solution
    @PutMapping("/{solutionId}/description")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SolutionDto> updateSolution(@PathVariable("solutionId") Long solutionId, @RequestBody SolutionDto solutionDto) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        // Check if the current user is the owner of the solution
        SolutionDto existingSolution = solutionService.getSolutionWithVotes(solutionId);
        if (!existingSolution.getUsername().equals(currentUsername)) {
            return ResponseEntity.status(403).build(); // Return 403 Forbidden if not the owner
        }
        
        SolutionDto updatedSolution = solutionService.updateSolution(solutionId, solutionDto.getDescription());
        return ResponseEntity.ok(updatedSolution);
    }

    // Endpoint to update the status of a solution
    @PutMapping("/{solutionId}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SolutionDto> updateSolutionStatus(
        @PathVariable("solutionId") Long solutionId, @RequestBody SolutionDto solutionDto) {
    	
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        // Check if the current user is the owner of the solution
        SolutionDto existingSolution = solutionService.getSolutionWithVotes(solutionId);
        if (existingSolution.getUsername().equals(currentUsername)) {
            return ResponseEntity.status(403).build(); // Return 403 Forbidden if not the owner
        }

        SolutionDto updatedSolution = solutionService.updateSolutionStatus(solutionId, solutionDto.getStatus());
        return ResponseEntity.ok(updatedSolution);
    }


 // Delete a solution by ID with ownership check
    @DeleteMapping("/{solutionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteSolution(@PathVariable("solutionId") Long solutionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        // Check if the current user is the owner of the solution
        SolutionDto existingSolution = solutionService.getSolutionWithVotes(solutionId);
        if (!existingSolution.getUsername().equals(currentUsername)) {
            return ResponseEntity.status(403).build(); // Return 403 Forbidden if not the owner
        }
        
        solutionService.deleteSolution(solutionId);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
    



}
