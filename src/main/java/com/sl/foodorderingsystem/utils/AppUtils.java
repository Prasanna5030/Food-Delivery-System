package com.sl.foodorderingsystem.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AppUtils {

    private AppUtils(){
    }

    public static ResponseEntity<String> getResponse(String response, HttpStatus status){
        return new ResponseEntity<>("{\"message\":\""+response+"\"}", status);
    }
}
