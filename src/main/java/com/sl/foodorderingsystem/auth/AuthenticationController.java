package com.sl.foodorderingsystem.auth;


import com.sl.foodorderingsystem.constants.AppConstants;
import com.sl.foodorderingsystem.dto.UserDto;
import com.sl.foodorderingsystem.service.UserService;
import com.sl.foodorderingsystem.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthenticationController {

    private final UserService userService;


    @GetMapping("/hello")
    public String hello() {
        return "hello user !!!";
    }


    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody(required = true) Map<String, String> registerRequest) {
        if (validateSignUp(registerRequest)) {
            return userService.signUp(registerRequest);

        } else {
            return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse("Bad Request ", HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody(required = true) Map<String, String> authenticationRequest) {
        if (validateLogin(authenticationRequest)) {
           return userService.login(authenticationRequest);
        } else {
            var authResponse = AuthenticationResponse.builder()
                    .message("Invalid User, Please Register")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();


            return new ResponseEntity<AuthenticationResponse>(authResponse, HttpStatus.BAD_REQUEST);
        }
    }







    public Boolean validateSignUp( Map<String , String> registerRequest){
        if(registerRequest.containsKey("name") && registerRequest.containsKey("email") && registerRequest.containsKey("password")
                && registerRequest.containsKey("contactNumber")){
            return true;
        }
        return false;
    }

    public Boolean validateLogin(Map<String , String> authencationRequest){
        if(authencationRequest.containsKey("email") && authencationRequest.containsKey("password") ){
            return true;
        }
        return false;
    }


}
