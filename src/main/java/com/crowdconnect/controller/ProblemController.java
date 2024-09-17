package com.crowdconnect.controller;

import com.crowdconnect.model.Problem;
import com.crowdconnect.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.security.Principal;


import java.util.List;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins = "http://localhost:3000")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @PostMapping
    @PreAuthorize("isAuthenticated()") // Ensure user is logged in
    public ResponseEntity<Problem> createProblem(@RequestBody Problem problem, Principal principal) {
        String username = principal.getName(); // Get the logged-in user's username
        Problem createdProblem = problemService.createProblem(problem, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProblem);
    }

    @GetMapping
    public ResponseEntity<List<Problem>> getAllProblems() {
        List<Problem> problems = problemService.getAllProblems();
        return new ResponseEntity<>(problems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable("id") Long id) {
        Problem problem = problemService.getProblemById(id);
        if (problem == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(problem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Problem> updateProblem(@PathVariable("id") Long id, @RequestBody Problem problemDetails) {
        // Find the existing problem by ID using the service
        Problem existingProblem = problemService.getProblemById(id);
        
        if (existingProblem == null) {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }

        // Retain the user from the existing problem if not provided in the request
        if (problemDetails.getUser() == null) {
            problemDetails.setUser(existingProblem.getUser());
        }

        // Update the problem fields you want to modify
        existingProblem.setTitle(problemDetails.getTitle());
        existingProblem.setDescription(problemDetails.getDescription());
        // Update any other fields you want (remove getOtherFields())

        // Save the updated problem
        Problem updatedProblem = problemService.updateProblem(id, existingProblem);

        return ResponseEntity.ok(updatedProblem); // Return the updated problem in the response
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable("id") Long id) {
        if (!problemService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }


}
