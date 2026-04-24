package com.Internproject.some.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Internproject.some.entity.Comment;
import com.Internproject.some.entity.Post;
import com.Internproject.some.repository.BotRepository;
import com.Internproject.some.repository.CommentRepository;
import com.Internproject.some.repository.PostRepository;
import com.Internproject.some.repository.UserRepositories;

import java.util.List;
 
@Service
public class CommentService {
 
    private static final int MAX_DEPTH = 20;
 
    private final CommentRepository commentRepository;
    private final PostRepository    postRepository;
    private final UserRepositories    userRepository;
    private final BotRepository     botRepository;
    private final ViralityService   viralityService;
 
    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepositories userRepository,
                          BotRepository botRepository,
                          ViralityService viralityService) {
        this.commentRepository = commentRepository;
        this.postRepository    = postRepository;
        this.userRepository    = userRepository;
        this.botRepository     = botRepository;
        this.viralityService   = viralityService;
    }
 

    public Comment addUserComment(Long postId, Long userId, String content, int depthLevel) {
 
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
 
       
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
 
       
        if (depthLevel > MAX_DEPTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Comment thread too deep. Max depth is " + MAX_DEPTH);
        }
 

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setAuthorId(userId);
        comment.setAuthorType("USER");
        comment.setContent(content);
        comment.setDepthLevel(depthLevel);
        Comment saved = commentRepository.save(comment);
 
        viralityService.addHumanCommentScore(postId);
 
        return saved;
    }

    public Comment addBotComment(Long postId, Long botId, Long humanOwnerId, String content, int depthLevel) {

        postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
 
        
        var bot = botRepository.findById(botId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bot not found"));
 
        
        boolean allowed = viralityService.incrementBotCountAndCheck(postId);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Post has reached the maximum of 100 bot replies.");
        }
 
      
        if (depthLevel > MAX_DEPTH) {
            
            viralityService.getBotCount(postId); 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Comment thread too deep. Max depth is " + MAX_DEPTH);
        }
 
        if (viralityService.isCooldownActive(botId, humanOwnerId)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Bot " + botId + " is on cooldown for human " + humanOwnerId + ". Try again after 10 minutes.");
        }
 
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setAuthorId(botId);
        comment.setAuthorType("BOT");
        comment.setContent(content);
        comment.setDepthLevel(depthLevel);
        Comment saved = commentRepository.save(comment);

        viralityService.addBotReplyScore(postId);
        String message = "Bot " + bot.getName() + " replied to your post #" + postId;
        viralityService.handleNotification(humanOwnerId, message);
 
        return saved;
    }
 
    public List<Comment> getCommentsForPost(Long postId) {
        return commentRepository.findAll()
                .stream()
                .filter(c -> c.getPostId().equals(postId))
                .toList();
    }
}
 