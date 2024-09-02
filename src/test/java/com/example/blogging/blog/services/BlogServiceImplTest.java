package com.example.blogging.blog.services;

import com.example.blogging.blog.entities.Blog;
import com.example.blogging.blog.entities.Tags;
import com.example.blogging.blog.repositories.BlogRepository;
import com.example.blogging.blog.requests.BlogPost;
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
import java.util.Optional;

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

    BlogPost blogPostRequest(Blog blog) {
        return new BlogPost(
                blog.getTittle(),
                blog.getContent(),
                blog.getCategory(),
                blog.getTags().tags()
        );
    }

    @Test
    void whenAllFieldsAreValid_createNewBlogPost() {
        // initialize the blog item
        Blog blog = blog();
        BlogPost request = blogPostRequest(blog);

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
        Blog blog = blog();
        blog.setTittle("");
        BlogPost request = blogPostRequest(blog);

        when(blogRepository.save(any(Blog.class))).thenThrow(BlogPostException.class);

        BlogPostException exception = assertThrows(BlogPostException.class, () -> blogService.createNewBlogPost(request));

        // assertions
        assertNotNull(request);
        assertEquals(Causes.NO_EMPTY_FIELDS_ALLOWED.label, exception.getMessage());
        assertTrue(exception.getCause().getMessage().contains("title"));
    }

    @Test
    void whenContentFieldIsEmpty_ThrowNoEmptyFieldAllowedExceptionWithTheContentField() {
        Blog blog = blog();
        blog.setContent("");
        BlogPost request = blogPostRequest(blog);

        when(blogRepository.save(any(Blog.class))).thenThrow(BlogPostException.class);

        BlogPostException exception = assertThrows(BlogPostException.class, () -> blogService.createNewBlogPost(request));

        // assertions
        assertNotNull(request);
        assertEquals(Causes.NO_EMPTY_FIELDS_ALLOWED.label, exception.getMessage());
        assertTrue(exception.getCause().getMessage().contains("content"));
    }

    @Test
    void whenCategoryFieldIsEmpty_ThrowNoEmptyFieldAllowedExceptionWithTheCategoryField() {
        Blog blog = blog();
        blog.setCategory("");
        BlogPost request = blogPostRequest(blog);

        when(blogRepository.save(any(Blog.class))).thenThrow(BlogPostException.class);

        BlogPostException exception = assertThrows(BlogPostException.class, () -> blogService.createNewBlogPost(request));

        // assertions
        assertNotNull(request);
        assertEquals(Causes.NO_EMPTY_FIELDS_ALLOWED.label, exception.getMessage());
        assertTrue(exception.getCause().getMessage().contains("category"));
    }

    @Test
    void whenTheBlogEntityIsEmpty_ThrowNullItemReceivedException() {

        when(blogRepository.save(any(Blog.class))).thenThrow(BlogPostException.class);

        BlogPostException exception = assertThrows(BlogPostException.class, () -> blogService.postResponse(HttpStatus.BAD_REQUEST, null));

        // assertions
        assertNotNull(exception);
        assertEquals(Causes.NULL_ITEM_RECEIVED.label, exception.getMessage());
    }

    @Test
    void whenAllFieldsAreValidAndThePostIdExists_updateBlogPost() {
        // initialize the blog item
        Blog blog = blog();
        BlogPost request = blogPostRequest(blog);

        // mock the save operation
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);
        when(blogRepository.findById(blog().getId())).thenReturn(Optional.of(blog));

        // perform the blog creation operation
        ResponseEntity<Response<CreatedBlogPostData>> response = blogService.updateBlogPost(blog().getId(), request);

        // assertions
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getMessage());

        assertNotNull(response.getBody().getData().getCategory());
        assertNotNull(response.getBody().getData().getContent());
        assertNotNull(response.getBody().getData().getTitle());
    }

    @Test
    void whenTheBlogIdDoesNotExist_Throw_BLOG_ID_DOES_NOT_EXIST_Exception() {
        // initialize the blog item
        Blog blog = blog();
        BlogPost request = blogPostRequest(blog);

        // mock the save operation
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);
        when(blogRepository.findById(blog().getId())).thenReturn(Optional.of(blog));

        // perform the blog creation operation
        BlogPostException exception = assertThrows(BlogPostException.class, () -> blogService.updateBlogPost(3, request));

        // assertions
        assertNotNull(exception);
        assertEquals(Causes.BLOG_ID_DOES_NOT_EXIST.label, exception.getMessage());
        assertEquals("The submitted id is not in the system", exception.getCause().getMessage());
    }
}