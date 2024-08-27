package com.example.blogging.blog.services;

import com.example.blogging.blog.entities.Blog;
import com.example.blogging.blog.entities.Tags;
import com.example.blogging.blog.requests.NewBlogPostRequest;
import com.example.blogging.blog.responses.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceImplTest {
    @InjectMocks
    BlogServiceImpl blogService;

    // create a custom blog item for testing
    Blog blog() {
        // create a custom tag entity
        Tags tags = new Tags(
                List.of(
                        "#software",
                        "#funky_developer",
                        "#letscodetogether",
                        "#java"
                )
        );

        // create new blog entity
        Blog blog = new Blog();
        blog.setId(UUID.randomUUID().toString());
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        blog.setTittle("Unit Testing");
        blog.setContent("All programmers must practice unit testing!");
        blog.setCategory("Software Development");
        blog.setTags(tags);

        return blog;
    }

    NewBlogPostRequest blogPostRequest(Blog blog) {
        return new NewBlogPostRequest(
                blog.getTittle(),
                blog.getContent(),
                blog.getCategory(),
                blog.getTags().tags()
        );
    }

    Response createdBlogPostResponse() {
        Blog blog = blog();
        NewBlogPostRequest blogPostRequest = blogPostRequest(blog);
        return Response
                .builder()
                .id(blog.getId())
                .title(blogPostRequest.title())
                .category(blogPostRequest.category())
                .tags(blogPostRequest.tags())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }

    @Test
    void whenAllFieldsAreValid_createNewBlogPost() {
        // initialize the blog item
        Blog blog = blog();

        // perform the blog creation operation
        ResponseEntity<Response> response = blogService.createNewBlogPost(blogPostRequest(blog));
        System.out.println(createdBlogPostResponse());

        // assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}