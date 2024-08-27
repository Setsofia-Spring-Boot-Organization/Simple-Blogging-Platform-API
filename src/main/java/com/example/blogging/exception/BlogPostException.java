package com.example.blogging.exception;

public class BlogPostException extends RuntimeException{
    public BlogPostException(String cause, Throwable message) {
        super(cause, message);
    }

    public BlogPostException(Causes cause) {
        super(cause.label);
    }
}
