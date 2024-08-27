package com.example.blogging.blog.requests;

import java.util.List;

/**
 * Represents a request to create a new blog post.
 * Contains details for the new post, including the title, content,
 * category, and tags associated with the post.
 *
 * @param title   The title of the blog post.
 * @param content The main content or body of the blog post.
 * @param category The category under which the blog post falls.
 * @param tags    A list of tags associated with the blog post.
 */
public record NewBlogPostRequest(
        String title,
        String content,
        String category,
        List<String> tags
) {}
