package com.ttn.bootcamp.project.bootcampproject.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PasswordMismatch extends RuntimeException {
        public PasswordMismatch(String message){
            super(message);
        }
}
