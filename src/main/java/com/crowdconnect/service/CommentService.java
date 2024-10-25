package com.crowdconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.crowdconnect.dto.CommentDto;
import com.crowdconnect.model.Comment;
import com.crowdconnect.model.Solution;
import com.crowdconnect.model.User;
import com.crowdconnect.repository.CommentRepository;
import com.crowdconnect.repository.SolutionRepository;
import com.crowdconnect.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SolutionRepository solutionRepository;

    public CommentDto addComment(Long solutionId, CommentDto commentDto) {
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(() -> new RuntimeException("Solution not found"));

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setSolution(solution);
        comment.setCreatedAt(LocalDateTime.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);

        commentDto.setId(savedComment.getId());
        commentDto.setCreatedAt(savedComment.getCreatedAt());
        commentDto.setUsername(user.getUsername());
        commentDto.setSolutionId(solution.getId());

        return commentDto;
    }

    public List<CommentDto> getCommentsBySolutionId(Long solutionId) {
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(() -> new RuntimeException("Solution not found"));
        return commentRepository.findBySolution(solution)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CommentDto updateComment(Long commentId, CommentDto updatedCommentDto) {
        Optional<Comment> existingCommentOptional = commentRepository.findById(commentId);
        if (existingCommentOptional.isPresent()) {
            Comment existingComment = existingCommentOptional.get();
            existingComment.setContent(updatedCommentDto.getContent());

            Comment updatedComment = commentRepository.save(existingComment);
            return mapToDto(updatedComment);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> existingCommentOptional = commentRepository.findById(commentId);
        if (existingCommentOptional.isPresent()) {
            commentRepository.deleteById(commentId);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }
    
    public boolean isCommentOwner(Long commentId, String username) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            return comment.getUser().getUsername().equals(username);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }


    private CommentDto mapToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setSolutionId(comment.getSolution().getId());
        dto.setUsername(comment.getUser().getUsername());
        return dto;
    }
}
