package com.example.activity_service.dto.response;

import com.example.activity_service.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewsFeedDto {

    private Long id; // pk
    private String title;
    private String content;
    private String writer;

    @Builder
    private NewsFeedDto(Long id, String title, String content, String writer){
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public static NewsFeedDto of(Post post){
        return NewsFeedDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getUserId())
                .build();
    }
}
