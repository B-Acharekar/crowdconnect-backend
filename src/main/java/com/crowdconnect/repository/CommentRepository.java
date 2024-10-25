package com.crowdconnect.repository;

import com.crowdconnect.model.Comment;
import com.crowdconnect.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySolution(Solution solution);
}
