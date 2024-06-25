package com.sl.foodorderingsystem.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/home")
public class UserRestController {

  @GetMapping("/hi")
  public String home(){
    return "Hello from secured endpoint";
}


    }











