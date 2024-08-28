package com.example.blogging.blog.services;

import com.example.blogging.blog.entities.Blog;
import com.example.blogging.blog.entities.Tags;
import com.example.blogging.blog.repositories.BlogRepository;
import com.example.blogging.blog.requests.NewBlogPostRequest;
import com.example.blogging.blog.responses.CreatedBlogPostData;
import com.example.blogging.blog.responses.Response;
import com.example.blogging.exception.BlogPostException;
import com.example.blogging.exception.Causes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BlogServiceImplTest {
    @Mock
    BlogRepository blogRepository;

    @InjectMocks
    BlogServiceImpl blogService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
        Mockito.reset(blogRepository);
    }

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
        blog.setId(1);
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

    ResponseEntity<Response<CreatedBlogPostData>> createdBlogPostResponse() {
        Blog blog = blog();
        NewBlogPostRequest blogPostRequest = blogPostRequest(blog);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.<CreatedBlogPostData>builder()
                .status(HttpStatus.CREATED.value())
                .data(CreatedBlogPostData
                        .builder()
                        .id(blog.getId())
                        .title(blogPostRequest.title())
                        .category(blogPostRequest.category())
                        .tags(blogPostRequest.tags())
                        .createdAt(blog.getCreatedAt())
                        .updatedAt(blog.getUpdatedAt())
                        .build()
                )
                .build());
    }

    @Test
    void whenAllFieldsAreValid_createNewBlogPost() {
        // initialize the blog item
        Blog blog = blog();
        NewBlogPostRequest request = blogPostRequest(blog);

        // mock the save operation
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        // perform the blog creation operation
        ResponseEntity<Response<CreatedBlogPostData>> response = blogService.createNewBlogPost(request);

        // assertions
        assertNotNull(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void whenTitleFieldIsEmpty_ThrowNoEmptyFieldAllowedExceptionWithTheTitleField() {
        // initialize the blog item
        Blog blog = blog();
        blog.setTittle("");
        NewBlogPostRequest request = blogPostRequest(blog);

        // mock the save operation
        when(blogRepository.save(any(Blog.class))).thenThrow(BlogPostException.class);

        // perform the blog creation operation
        BlogPostException exception = assertThrows(BlogPostException.class, () -> blogService.createNewBlogPost(request));

        // assertions
        assertNotNull(request);
        assertEquals(Causes.NO_EMPTY_FIELDS_ALLOWED.label, exception.getMessage());
        assertTrue(exception.getCause().getMessage().contains("title"));
    }

    @Test
    void whenContentFieldIsEmpty_ThrowNoEmptyFieldAllowedExceptionWithTheContentField() {
        // initialize the blog item
        Blog blog = blog();
        blog.setContent("");
        NewBlogPostRequest request = blogPostRequest(blog);

        // mock the save operation
        when(blogRepository.save(any(Blog.class))).thenThrow(BlogPostException.class);

        // perform the blog creation operation
        BlogPostException exception = assertThrows(BlogPostException.class, () -> blogService.createNewBlogPost(request));

        // assertions
        assertNotNull(request);
        assertEquals(Causes.NO_EMPTY_FIELDS_ALLOWED.label, exception.getMessage());
        assertTrue(exception.getCause().getMessage().contains("content"));
    }

    @Test
    void whenCategoryFieldIsEmpty_ThrowNoEmptyFieldAllowedExceptionWithTheCategoryField() {
        // initialize the blog item
        Blog blog = blog();
        blog.setCategory("");
        NewBlogPostRequest request = blogPostRequest(blog);

        // mock the save operation
        when(blogRepository.save(any(Blog.class))).thenThrow(BlogPostException.class);

        // perform the blog creation operation
        BlogPostException exception = assertThrows(BlogPostException.class, () -> blogService.createNewBlogPost(request));

        // assertions
        assertNotNull(request);
        assertEquals(Causes.NO_EMPTY_FIELDS_ALLOWED.label, exception.getMessage());
        assertTrue(exception.getCause().getMessage().contains("category"));
    }
}