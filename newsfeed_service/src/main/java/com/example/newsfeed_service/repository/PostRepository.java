package com.example.newsfeed_service.repository;

import com.example.newsfeed_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
