package com.example.newsfeed_service.service;


import static com.example.newsfeed_service.common.response.BaseResponseStatus.POST_ID_INVALID;
import static com.example.newsfeed_service.common.response.BaseResponseStatus.USERS_INVALID_EMAIL;

import com.example.newsfeed_service.common.exceptions.BaseException;
import com.example.newsfeed_service.common.jwt.JWTUtil;
import com.example.newsfeed_service.domain.ActiveType;
import com.example.newsfeed_service.dto.request.CreatePostReq;
import com.example.newsfeed_service.dto.response.CreatePostRes;
import com.example.newsfeed_service.entity.LikePost;
import com.example.newsfeed_service.entity.Post;
import com.example.newsfeed_service.entity.User;
import com.example.newsfeed_service.entity.UserLog;
import com.example.newsfeed_service.repository.FeedRepository;
import com.example.newsfeed_service.repository.LikePostRepository;
import com.example.newsfeed_service.repository.PostRepository;
import com.example.newsfeed_service.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final LikePostRepository likePostRepository;
    private final FeedRepository feedRepository;

    public CreatePostRes createPost(String userEmail, CreatePostReq createPostReq) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BaseException(USERS_INVALID_EMAIL));

        Post buildPost = Post.builder()
                .title(createPostReq.getTitle())
                .content(createPostReq.getContent())
                .user(user)
                .build();

        Post post = postRepository.save(buildPost);

        String log = user.getName() + "님이 " + post.getTitle() + " 이라는 제목의 글을 작성했습니다.";

        UserLog userLog = UserLog.builder()
                .actor(user)
                .recipient(post.getUser())
                .log(log)
                .activeType(ActiveType.WRITE_POST)
                .build();
        feedRepository.save(userLog);

        return CreatePostRes.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getUser().getName())
                .build();

    }

    public String likePost(String userEmail, Long postId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BaseException(USERS_INVALID_EMAIL));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(POST_ID_INVALID));

        //  자기 자신의 글은 좋아요 불가
//        if(Objects.equals(user.getId(), post.getUser().getId())){
//            return "자기 자신의 글은 좋아요를 할수 없습니다.";
//        }
        String userName = user.getName();
        String postUserName = post.getUser().getName();

        Optional<LikePost> byUserAndPost = likePostRepository.findByUserAndPost(user, post);

        if (!byUserAndPost.isPresent()) {
            // 객체 생성
            LikePost likePost = LikePost.builder()
                    .user(user)
                    .post(post)
                    .build();

            likePostRepository.save(likePost);

            String log = userName + "님이 " + postUserName + "님의 " + post.getTitle() + "제목의 글을 좋아합니다.";

            UserLog userLog = UserLog.builder()
                    .actor(user)
                    .recipient(post.getUser())
                    .log(log)
                    .activeType(ActiveType.LIKE_POST)
                    .build();
            feedRepository.save(userLog);

            return "해당 글에 좋아요를 완료했습니다.";

        }
        // todo : 나중에 state 상태 변경으로 바꾸기.
        likePostRepository.delete(byUserAndPost.get());

        String log = userName + "님이 " + postUserName + "님의 " + post.getTitle() + "제목의 글 좋아요를 취소했습니다.";

        UserLog userLog = UserLog.builder()
                .actor(user)
                .recipient(post.getUser())
                .log(log)
                .activeType(ActiveType.CANCEL_LIKE_POST)
                .build();
        feedRepository.save(userLog);

        return "해당 글에 좋아요를 취소했습니다.";

    }
}