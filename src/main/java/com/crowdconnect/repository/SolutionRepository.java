package com.crowdconnect.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.crowdconnect.model.Solution;
import com.crowdconnect.model.Problem;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findByProblem(Problem problem);
}
