package com.example.blogging.blog.entities;

import jakarta.persistence.Embeddable;

import java.util.List;
@Embeddable
public record Tags(
        List<String> tags
) {}
