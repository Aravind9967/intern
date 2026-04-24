package com.Internproject.some.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Internproject.some.entity.Post;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	//List<Post> findByUserAuthorId(Long userId);
	//List<Post> findByBotAuthorId(Long botId);

}
