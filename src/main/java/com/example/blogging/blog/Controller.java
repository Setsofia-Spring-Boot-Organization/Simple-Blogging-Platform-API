package com.example.blogging.blog;

import com.example.blogging.blog.requests.BlogPost;
import com.example.blogging.blog.responses.CreatedBlogPostData;
import com.example.blogging.blog.responses.Response;
import com.example.blogging.blog.services.BlogServiceImpl;
import com.example.blogging.exception.BlogPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "blog/api/v1/posts")
public class Controller {

    private final BlogServiceImpl blogServiceImpl;

    @PostMapping
    public ResponseEntity<Response<CreatedBlogPostData>> createBlogPost(
            @Validated @RequestBody BlogPost request
    ) throws BlogPostException {
        return blogServiceImpl.createNewBlogPost(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<CreatedBlogPostData>> updateBlogPost(
            @PathVariable int id,
            @Validated @RequestBody BlogPost request
    ) throws BlogPostException {
        return blogServiceImpl.updateBlogPost(id, request);
    }
}
