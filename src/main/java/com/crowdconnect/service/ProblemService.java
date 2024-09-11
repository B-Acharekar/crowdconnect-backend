package com.crowdconnect.service;

import com.crowdconnect.model.Problem;
import com.crowdconnect.model.User;
import com.crowdconnect.repository.ProblemRepository;
import com.crowdconnect.repository.UserRepository;
import com.crowdconnect.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {
    @Autowired
    private ProblemRepository problemRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Problem createProblem(Problem problem, String username) {
        // Find the user who posted the problem
    	User user = userRepository.findByUsername(username)
    		    .orElseThrow(() -> new RuntimeException("User not found"));

        problem.setUser(user); // Associate the problem with the user
        return problemRepository.save(problem);
    }

    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    public Problem getProblemById(Long id) {
        return problemRepository.findById(id).orElse(null);
    }

    public Problem updateProblem(Long id, Problem problem) {
        if (problemRepository.existsById(id)) {
            problem.setId(id); // Ensure the ID is set correctly
            return problemRepository.save(problem);
        }
        return null; // Or throw an exception
    }

    
    public void deleteProblem(Long id) {
        if (!problemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Problem not found");
        }
        problemRepository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return problemRepository.existsById(id);
    }
    
}
