package com.example.blogging.blog.services;

import com.example.blogging.blog.entities.Blog;
import com.example.blogging.blog.entities.Tags;
import com.example.blogging.blog.interfaces.BlogService;
import com.example.blogging.blog.repositories.BlogRepository;
import com.example.blogging.blog.requests.BlogPost;
import com.example.blogging.blog.responses.CreatedBlogPostData;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Override
    public ResponseEntity<Response<CreatedBlogPostData>> createNewBlogPost(BlogPost request) {
        // verify that the input fields are not empty
        List<String> inputFields = validateInputFields(request);
        if (!inputFields.isEmpty()) throw new BlogPostException(Causes.NO_EMPTY_FIELDS_ALLOWED, new Throwable(Causes.THE_FOLLOWING_FIELDS_ARE_EMPTY.label + inputFields));

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

        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse(HttpStatus.CREATED, createdBlog));
    }

    @Override
    public ResponseEntity<Response<CreatedBlogPostData>> updateBlogPost(int id,  BlogPost request) {

        // validate the request fields
        List<String> inputFields = validateInputFields(request);
        if (!inputFields.isEmpty()) throw new BlogPostException(Causes.NO_EMPTY_FIELDS_ALLOWED, new Throwable(Causes.THE_FOLLOWING_FIELDS_ARE_EMPTY.label + inputFields));

        // update the blog
        Blog updatedBlog = updateBlog(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(postResponse(HttpStatus.OK, updatedBlog));
    }

    // helper methods:
    /**
     * Validates the input fields of the provided blog post request.
     * Checks for any empty mandatory fields such as title, category, and content.
     *
     * @param postRequest The request object containing the details of the blog post to be validated.
     * @return A list of field names that are empty. If all fields are filled, the list will be empty.
     */
    private <T extends BlogPost> List<String>  validateInputFields(T postRequest) {
        List<String> emptyFields = new ArrayList<>();

        if (postRequest.title().isEmpty()) emptyFields.add("title");
        if (postRequest.category().isEmpty()) emptyFields.add("category");
        if (postRequest.content().isEmpty()) emptyFields.add("content");

        return emptyFields;
    }

    /**
     * This method updates an existing Blog entity with new data provided in the BlogPost request.
     * It updates the blog's title, content, category, and tags, and sets the updated timestamp.
     * The updated blog is then saved to the repository.
     *
     * @param id the existing blog to be updated id
     * @param request the new data for the blog post
     * @return the updated Blog entity after it has been saved to the repository
     */
    private Blog updateBlog(int id, BlogPost request) {
        // confirm the blog exists
        Blog blog = getBlogById(id);

        blog.setUpdatedAt(LocalDateTime.now());
        blog.setTittle(request.title());
        blog.setContent(request.content());
        blog.setCategory(request.category());
        blog.setTags(new Tags(request.tags()));

        // save the created blog
       return blogRepository.save(blog);
    }

    /**
     * Constructs a response for a created blog post using the provided blog entity.
     * The response includes the status, message, and details of the created blog post.
     *
     * @param blog The blog entity from which the response details are extracted.
     * @return A {@link Response} object containing the created blog post details.
     */
    Response<CreatedBlogPostData> postResponse(HttpStatus status, Blog blog) {
        // make sure the blog item/entity is not null before returning it
        if (blog == null) throw new BlogPostException(Causes.NULL_ITEM_RECEIVED);

        return Response.<CreatedBlogPostData>builder()
                .status(status.value())
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

    /**
     * This method retrieves a Blog entity by its ID.
     * If the blog with the given ID does not exist, it throws a BlogPostException with the appropriate cause.
     *
     * @param id the ID of the blog to be retrieved
     * @return the Blog entity with the specified ID
     * @throws BlogPostException if a blog with the given ID does not exist
     */
    private Blog getBlogById(int id) throws BlogPostException {
        Optional<Blog> optionalPost = blogRepository.findById(id);
        if (optionalPost.isEmpty()) throw new BlogPostException(Causes.BLOG_ID_DOES_NOT_EXIST, new Throwable("The submitted id is not in the system"));

        return optionalPost.get();
    }
 }
