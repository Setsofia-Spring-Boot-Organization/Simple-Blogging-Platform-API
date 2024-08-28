package com.example.blogging.exception;

public class BlogPostException extends RuntimeException{

    final Causes cause;

    public BlogPostException(Causes cause, Throwable message) {
        super(cause.label, message);
        this.cause = cause;
    }

    public BlogPostException(Causes cause) { super(cause.label);
        this.cause = cause;
    }
}
