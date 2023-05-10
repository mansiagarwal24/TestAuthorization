package com.ttn.bootcamp.project.bootcampproject.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ResourcesNotFoundException extends RuntimeException {
    public ResourcesNotFoundException(String message){
        super(message);
    }

}
