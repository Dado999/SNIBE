package com.example.controllers;

import com.example.models.DTOs.CommentDTO;
import com.example.services.CommentService.CommentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @GetMapping("{category}")
    public ResponseEntity<Page<CommentDTO>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        logger.info("Getting comments for category: {}",category);
        Page<CommentDTO> comments = commentService.getByCategory(category, page, size);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/unapproved-comments")
    public ResponseEntity<List<CommentDTO>> getUnapprovedComments(){
        logger.info("Retrieving unapproved comments");
        return ResponseEntity.ok(commentService.findUnapprovedComments());
    }
    @PostMapping("/approve/{id}")
    public ResponseEntity<Map<String,String>> approveComment(@PathVariable Integer id){
        logger.info("Approving comment with the ID: {}" ,id);
        return ResponseEntity.ok(Map.of("message",commentService.approveComment(id)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String,String>> deleteComment(@PathVariable Integer id) {
        logger.info("Deleting comment with the ID: {}", id);
        commentService.deleteComment(id);
        return ResponseEntity.ok(Map.of("message","Comment deleted successfully!"));
    }
    @PostMapping("update/{id}")
    public ResponseEntity<Map<String,String>> updateComment(@PathVariable Integer id,
                                                @RequestBody Map<String, String> requestBody){
        logger.info("Attempting to update the comment with the ID {} with content : {}", id,requestBody.get("content"));
        String newContent = requestBody.get("content");
        if (newContent == null || newContent.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message","Content must not be empty!"));
        }
        return ResponseEntity.ok(Map.of("message",commentService.updateComment(id, newContent)));
    }
    @PostMapping("/insert")
    public ResponseEntity<Map<String,String>> insertComment(@RequestBody CommentDTO commentDTO){
        logger.info("Inserting a new comment");
        return ResponseEntity.ok(Map.of("message",commentService.insertComment(commentDTO)));
    }
}
