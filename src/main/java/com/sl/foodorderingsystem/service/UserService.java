package com.sl.foodorderingsystem.service;

import com.sl.foodorderingsystem.auth.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {

    public ResponseEntity<AuthenticationResponse> signUp(Map<String , String> requestMap);

    public ResponseEntity<AuthenticationResponse> login(Map<String ,String> authenticateRequest);
}
