package com.example.blogging.blog.services;

import com.example.blogging.blog.entities.Blog;
import com.example.blogging.blog.entities.Tags;
import com.example.blogging.blog.interfaces.BlogService;
import com.example.blogging.blog.repositories.BlogRepository;
import com.example.blogging.blog.requests.NewBlogPostRequest;
import com.example.blogging.blog.responses.CreatedBlogPost;
import com.example.blogging.blog.responses.Response;
import com.example.blogging.exception.BlogPostException;
import com.example.blogging.exception.Causes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Override
    public ResponseEntity<Response<CreatedBlogPost>> createNewBlogPost(NewBlogPostRequest request) {
        // verify that the input fields are not empty
        List<String> inputFields = validateInputFields(request);
        if (!inputFields.isEmpty()) throw new BlogPostException(Causes.THE_FOLLOWING_FIELDS_ARE_EMPTY.label + inputFields, new Throwable(Causes.NO_EMPTY_FIELDS_ALLOWED.label));

        // create the new blog post
        Blog blog = new Blog();
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        blog.setTittle(request.title());
        blog.setContent(request.content());
        blog.setCategory(request.category());
        blog.setTags(new Tags(request.tags()));

        // save the created blog
        Blog createdBlog = blogRepository.save(blog);

        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse(createdBlog));
    }

    // helper methods:
    /**
     * Validates the input fields of the provided blog post request.
     * Checks for any empty mandatory fields such as title, category, and content.
     *
     * @param postRequest The request object containing the details of the blog post to be validated.
     * @return A list of field names that are empty. If all fields are filled, the list will be empty.
     */
    List<String> validateInputFields(NewBlogPostRequest postRequest) {
        List<String> emptyFields = new ArrayList<>();

        if (postRequest.title().isEmpty()) emptyFields.add("title");
        if (postRequest.category().isEmpty()) emptyFields.add("category");
        if (postRequest.content().isEmpty()) emptyFields.add("content");

        return emptyFields;
    }

    /**
     * Constructs a response for a created blog post using the provided blog entity.
     * The response includes the status, message, and details of the created blog post.
     *
     * @param blog The blog entity from which the response details are extracted.
     * @return A {@link Response} object containing the created blog post details.
     */
    Response<CreatedBlogPost> postResponse(Blog blog) {
        return Response.<CreatedBlogPost>builder()
                .status(HttpStatus.CREATED.value())
                .message("success")
                .data(CreatedBlogPost
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
 }
