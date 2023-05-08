package com.ttn.bootcamp.project.bootcampproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;
@Service
public class I18Service {
    @Autowired
    private MessageSource messageSource;

    public String getMsg(String message){
        Locale locale= LocaleContextHolder.getLocale();
        return messageSource.getMessage(message,null,"Message Not Found",locale);
    }
}
