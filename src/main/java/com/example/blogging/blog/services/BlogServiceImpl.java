package com.example.blogging.blog.services;

import com.example.blogging.blog.interfaces.BlogService;
import com.example.blogging.blog.requests.NewBlogPostRequest;
import com.example.blogging.blog.responses.CreatedBlogPostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {

    @Override
    public ResponseEntity<CreatedBlogPostResponse> createNewBlogPost(NewBlogPostRequest request) {


        return null;
    }
}
