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

import com.Internproject.some.entity.Post;
import com.Internproject.some.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
 
    private final PostService postService;
 
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Map<String, Object> body) {
        Long authorId    = Long.valueOf(body.get("authorId").toString());
        String authorType = (String) body.get("authorType");
        String content   = (String) body.get("content");
 
        Post post;
        if ("BOT".equalsIgnoreCase(authorType)) {
            post = postService.createBotPost(authorId, content);
        } else {
            post = postService.createUserPost(authorId, content);
        }
        return ResponseEntity.ok(post);
    }
    
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }
 
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.likePost(postId));
    }
}
