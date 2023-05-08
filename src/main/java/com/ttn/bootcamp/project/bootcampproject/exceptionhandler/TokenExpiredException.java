package com.ttn.bootcamp.project.bootcampproject.exceptionhandler;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
