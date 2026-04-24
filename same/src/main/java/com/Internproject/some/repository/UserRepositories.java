package com.Internproject.some.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Internproject.some.entity.User;
@Repository
public interface UserRepositories extends JpaRepository<User, Long> {
	User findByUsername(String username);

}
