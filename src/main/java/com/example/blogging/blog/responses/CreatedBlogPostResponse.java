package com.example.blogging.blog.responses;

import lombok.Builder;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@ToString
public class CreatedBlogPostResponse {
    private String id;
    private String title;
    private String category;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
