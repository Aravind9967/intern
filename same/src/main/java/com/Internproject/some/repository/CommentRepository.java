package com.Internproject.some.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Internproject.some.entity.Comment;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
//	  List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
//	    List<Comment> findByPostIdAndDepthLevel(Long postId, int depthLevel);
//	    List<Comment> findByUserAuthorId(Long userId);
//	    List<Comment> findByBotAuthorId(Long botId);

}
