package com.example.blogging.blog.interfaces;

import com.example.blogging.blog.requests.NewBlogPostRequest;
import com.example.blogging.blog.responses.CreatedBlogPostData;
import com.example.blogging.blog.responses.Response;
import org.springframework.http.ResponseEntity;

public interface BlogService {

    /**
     * Creates a new blog post based on the provided request data.
     *
     * @param request The details of the new blog post, including title, content,
     *                category, and tags.
     * @return A {@link ResponseEntity} containing the response for the created blog post,
     *         including any relevant information about the newly created post.
     */
    ResponseEntity<Response<CreatedBlogPostData>> createNewBlogPost(NewBlogPostRequest request);
}
