package com.example.reservation.repository;

import com.example.reservation.entity.UserLog;
import com.example.reservation.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<UserLog, Long> {

//    Slice<Feed> findAllByUserIn(List<User> users, Pageable pageable); // 해당 유저정보를 갖고있는 피드 전체 가져옴. -> 에러

    //   List<Feed> findAllByUserInOrderByCreated_atDesc(List<User> users); -> 에러
    List<UserLog> findAllByActorIn(List<User> users);

}
