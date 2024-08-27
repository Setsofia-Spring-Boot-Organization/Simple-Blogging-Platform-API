package com.example.blogging.blog.responses;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@ToString
@EqualsAndHashCode
@Getter
public class CreatedBlogPost {
    private int id;
    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
