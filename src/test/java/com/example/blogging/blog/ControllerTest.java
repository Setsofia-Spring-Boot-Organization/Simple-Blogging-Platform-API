package com.example.blogging.blog;

import com.example.blogging.blog.entities.Blog;
import com.example.blogging.blog.entities.Tags;
import com.example.blogging.blog.repositories.BlogRepository;
import com.example.blogging.blog.requests.NewBlogPostRequest;
import com.example.blogging.blog.responses.CreatedBlogPostData;
import com.example.blogging.blog.responses.Response;
import com.example.blogging.blog.services.BlogServiceImpl;
import com.example.blogging.exception.BlogPostException;
import com.example.blogging.exception.Causes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class ControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    BlogRepository blogRepository;

    @Mock
    BlogServiceImpl blogService;

    private Controller blogController;

    private AutoCloseable autoCloseable;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        blogController = new Controller(blogService);
        mockMvc = MockMvcBuilders.standaloneSetup(blogController).build();
        objectMapper = new ObjectMapper();
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

    Response<CreatedBlogPostData> postResponse(Blog blog) {
        // make sure the blog item/entity is not null before returning it
        if (blog == null) throw new BlogPostException(Causes.NULL_ITEM_RECEIVED);

        return Response.<CreatedBlogPostData>builder()
                .status(HttpStatus.CREATED.value())
                .message("success")
                .data(CreatedBlogPostData
                        .builder()
                        .id(blog.getId())
                        .createdAt(blog.getCreatedAt())
                        .updatedAt(blog.getUpdatedAt())
                        .title(blog.getTittle())
                        .content(blog.getContent())
                        .category(blog.getCategory())
                        .tags(blog.getTags().tags())
                        .build())
                .build();
    }

    @Test
    void whenBlogPostIsCreated_ReturnCreatedHttpResponseWithTheCreatedData() throws Exception {
        Blog blog = blog();
        NewBlogPostRequest request = blogPostRequest(blog);

        when(blogService.createNewBlogPost(any(NewBlogPostRequest.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(postResponse(blog)));

        // sending the post request to the controller
        MvcResult postResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/blog/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        // assertions
        assertNotNull(postResult);
        assertEquals(HttpStatus.CREATED.value(), postResult.getResponse().getStatus());
    }

    @Test
    void whenAnyRequestFiledIsEmpty_ReturnBadRequestHttpResponse() throws Exception {
        Blog blog = blog();
        NewBlogPostRequest request = blogPostRequest(blog);

        // sending the post request to the controller
        MvcResult postResult = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/blog/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String body = postResult.getResponse().getContentAsString();
        System.out.println(body);
    }
}