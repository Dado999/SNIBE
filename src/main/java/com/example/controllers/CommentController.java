package com.example.controllers;

import com.example.models.DTOs.CommentDTO;
import com.example.services.CommentService.CommentService;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("{category}")
    public ResponseEntity<Page<CommentDTO>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,  // Default page is 0
            @RequestParam(defaultValue = "20") int size // Default size is 20
    ) {
        Page<CommentDTO> comments = commentService.getByCategory(category, page, size);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String,String>> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(Map.of("message","Comment deleted successfully!"));
    }

}
