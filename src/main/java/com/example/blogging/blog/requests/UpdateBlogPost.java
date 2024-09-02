package com.example.blogging.blog.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * This record represents the data required to update a blog post.
 * It includes the blog post's title, content, category, and tags.
 * <p>
 * The `@JsonIgnoreProperties(ignoreUnknown = true)` annotation ensures that any unknown properties in the JSON data
 * are ignored during deserialization.
 *
 * @param title the title of the blog post
 * @param content the content of the blog post
 * @param category the category to which the blog post belongs
 * @param tags the tags associated with the blog post
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateBlogPost(
        String title,
        String content,
        String category,
        List<String> tags
) {}
