package com.example.blogging.blog.interfaces;

import com.example.blogging.blog.entities.Blog;
import com.example.blogging.blog.requests.BlogPost;
import com.example.blogging.blog.responses.CreatedBlogPostData;
import com.example.blogging.blog.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BlogService {

    /**
     * Creates a new blog post based on the provided request data.
     *
     * @param request The details of the new blog post, including title, content,
     *                category, and tags.
     * @return A {@link ResponseEntity} containing the response for the created blog post,
     *         including any relevant information about the newly created post.
     */
    ResponseEntity<Response<CreatedBlogPostData>> createNewBlogPost(BlogPost request);

    /**
     * This method updates an existing blog post based on the provided request data.
     *
     * @param request the data for the blog post that needs to be updated
     * @return a ResponseEntity containing a Response object with the data of the updated blog post
     */
    ResponseEntity<Response<CreatedBlogPostData>> updateBlogPost(int id, BlogPost request);

    /**
     * This method deletes a blog post identified by the given ID.
     *
     * @param id the ID of the blog post to be deleted
     * @return a ResponseEntity with the HTTP status indicating the result of the deletion operation
     */
    ResponseEntity<HttpStatus> deleteBlogPost(int id);

    /**
     * This method retrieves a single blog post by its ID.
     *
     * @param id the ID of the blog post to be retrieved
     * @return a ResponseEntity containing a Response object with the data of the retrieved blog post
     */
    ResponseEntity<Response<CreatedBlogPostData>> getSingleBlogPostById(int id);

    /**
     * This method retrieves a list of blog posts that match the specified search term.
     *
     * @param term the search term used to filter the blog posts
     * @return a ResponseEntity containing a Response object with a list of data for the filtered blog posts
     */
    ResponseEntity<Response<List<Blog>>> getBlogPosts(String term);
}
