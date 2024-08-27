package com.example.blogging.blog;

import com.example.blogging.blog.requests.NewBlogPostRequest;
import com.example.blogging.blog.responses.CreatedBlogPost;
import com.example.blogging.blog.responses.Response;
import com.example.blogging.blog.services.BlogServiceImpl;
import com.example.blogging.exception.BlogPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "blog/api/v1/posts")
public class Controller {

    private final BlogServiceImpl blogServiceImpl;

    @PostMapping
    public ResponseEntity<Response<CreatedBlogPost>> createBlogPost(
            @RequestBody NewBlogPostRequest request
    ) throws BlogPostException {
        return blogServiceImpl.createNewBlogPost(request);
    }
}
