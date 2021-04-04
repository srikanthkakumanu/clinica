package com.srikanth.clinica.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

@RestController
public class DoctorController {
    
    @RequestMapping(method=RequestMethod.POST, value = "/register")
    public static String register(@RequestBody String name) {
        return name + " is added!";
    }

    @ExceptionHandler 
    void handleIllegalArgumentException(IllegalArgumentException e, HttpResponse response) throws IOException {
        response.setStatus(HttpResponseStatus.BAD_REQUEST);
        e.printStackTrace();
    }

}