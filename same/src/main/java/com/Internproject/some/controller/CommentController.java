package com.Internproject.some.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Internproject.some.entity.Comment;
import com.Internproject.some.service.CommentService;

@RestController
@RequestMapping("/api/posts")
public class CommentController {
 
    private final CommentService commentService;
 
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId,
                                              @RequestBody Map<String, Object> body) {
 
        String authorType = (String) body.get("authorType");
        Long authorId     = Long.valueOf(body.get("authorId").toString());
        String content    = (String) body.get("content");
        int depthLevel    = body.containsKey("depthLevel")
                            ? Integer.parseInt(body.get("depthLevel").toString()) : 0;
 
        Comment comment;
 
        if ("BOT".equalsIgnoreCase(authorType)) {
            Long humanOwnerId = Long.valueOf(body.get("humanOwnerId").toString());
            comment = commentService.addBotComment(postId, authorId, humanOwnerId, content, depthLevel);
        } else {
            comment = commentService.addUserComment(postId, authorId, content, depthLevel);
        }
 
        return ResponseEntity.ok(comment);
    }
 
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsForPost(postId));
    }
}