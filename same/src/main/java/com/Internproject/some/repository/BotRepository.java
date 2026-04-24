package com.Internproject.some.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Internproject.some.entity.Bot;
@Repository
public interface BotRepository extends JpaRepository<Bot, Long>{
	//Optional<Bot> findByname(String name);

}
