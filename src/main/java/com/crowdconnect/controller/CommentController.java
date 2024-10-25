package com.crowdconnect.controller;

import com.crowdconnect.dto.CommentDto;
import com.crowdconnect.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/solution/{solutionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> addComment(@PathVariable("solutionId") Long solutionId, @RequestBody CommentDto commentDto) {
        if (commentDto.getContent() == null) {
            return ResponseEntity.badRequest().body(null); // Return error if content is null
        }
    	CommentDto savedComment = commentService.addComment(solutionId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @GetMapping("/solutions/{solutionId}")
    @PreAuthorize("isAuthenticated()") // Ensuring the user is authenticated
    public List<CommentDto> getCommentsBySolutionId(@PathVariable("solutionId") Long solutionId) {
        return commentService.getCommentsBySolutionId(solutionId);
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()") // Ensuring the user is authenticated
    public ResponseEntity<CommentDto> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentDto commentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        // Check if the comment belongs to the authenticated user
        if (!commentService.isCommentOwner(commentId, currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 Forbidden if not the owner
        }

        CommentDto updatedComment = commentService.updateComment(commentId, commentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or @commentService.isCommentOwner(#commentId, principal.username))") // Allow admins or the comment owner to delete
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
