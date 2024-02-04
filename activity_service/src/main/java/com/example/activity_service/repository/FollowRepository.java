package com.example.activity_service.repository;

import com.example.activity_service.entity.Follow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);

    List<Follow> findAllByFromUser(User user);
}
