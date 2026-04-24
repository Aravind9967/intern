package com.Internproject.some.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Internproject.some.entity.Post;
import com.Internproject.some.repository.PostRepository;

@Service
public class PostService {
 
    private final PostRepository postRepository;
    private final ViralityService viralityService;
 
    public PostService(PostRepository postRepository, ViralityService viralityService) {
        this.postRepository = postRepository;
        this.viralityService = viralityService;
    }
 
    public Post createUserPost(Long userId, String content) {
        Post post = new Post();
        post.setAuthorId(userId);
        post.setAuthorType("USER");
        post.setContent(content);
        return postRepository.save(post);
    }
 
    public Post createBotPost(Long botId, String content) {
        Post post = new Post();
        post.setAuthorId(botId);
        post.setAuthorType("BOT");
        post.setContent(content);
        return postRepository.save(post);
    }
    public String likePost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));
        viralityService.addHumanLikeScore(postId);
        Long score = viralityService.getViralityScore(postId);
        return "Post liked. Virality score is now " + score;
    }
 
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));
    }
 
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}