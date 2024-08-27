package com.example.blogging.blog.responses;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@ToString
@Getter
public class Response<T> {
    private int status;
    private String message;
    private T data;
}
