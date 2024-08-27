package com.example.blogging.blog.services;

import com.example.blogging.blog.interfaces.BlogService;
import com.example.blogging.blog.requests.NewBlogPostRequest;
import com.example.blogging.blog.responses.CreatedBlogPostResponse;
import com.example.blogging.exception.BlogPostException;
import com.example.blogging.exception.Causes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Override
    public ResponseEntity<CreatedBlogPostResponse> createNewBlogPost(NewBlogPostRequest request) {
        // verify that the input fields are not empty
        List<String> inputFields = validateInputFields(request);
        if (!inputFields.isEmpty()) throw new BlogPostException(Causes.THE_FOLLOWING_FIELDS_ARE_EMPTY.label + inputFields, new Throwable(Causes.NO_EMPTY_FIELDS_ALLOWED.label));

        return null;
    }

    List<String> validateInputFields(NewBlogPostRequest postRequest) {
        List<String> emptyFields = new ArrayList<>();

        if (postRequest.title().isEmpty()) emptyFields.add("title");
        if (postRequest.category().isEmpty()) emptyFields.add("category");
        if (postRequest.content().isEmpty()) emptyFields.add("content");

        return emptyFields;
    }
}
