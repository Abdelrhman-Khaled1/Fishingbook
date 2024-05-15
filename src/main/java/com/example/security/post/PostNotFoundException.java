package com.example.security.post;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String s) {
        super(s);
    }
}
