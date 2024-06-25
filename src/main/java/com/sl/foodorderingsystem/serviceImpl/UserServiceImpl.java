package com.sl.foodorderingsystem.serviceImpl;

import com.sl.foodorderingsystem.JWT.JwtService;
import com.sl.foodorderingsystem.auth.AuthenticationResponse;
import com.sl.foodorderingsystem.constants.AppConstants;
import com.sl.foodorderingsystem.entity.User;
import com.sl.foodorderingsystem.repository.UserRepository;
import com.sl.foodorderingsystem.service.UserService;
import com.sl.foodorderingsystem.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<AuthenticationResponse> signUp(Map<String, String> requestMap) {
        try {
            Optional<User> user = userRepository.findByEmail(requestMap.get("email"));

            if (user.isPresent()) {
                var authresponse = AuthenticationResponse.builder().message(
                        "user already exists"
                ).status(HttpStatus.BAD_REQUEST).build();

                return new ResponseEntity<AuthenticationResponse>(authresponse, HttpStatus.BAD_REQUEST);

            } else {
                User signupUser = User.builder()
                        .name(requestMap.get("name"))
                        .contactNumber(requestMap.get("contactNumber"))
                        .email(requestMap.get("email"))
                        .password(passwordEncoder.encode(requestMap.get("password")))
                        .status("false")
                        .role("USER")
                        .build();

               User user1= userRepository.save(signupUser);

                Map<String, Object> claims = new HashMap<>();
                claims.put("role", user1.getRole());

                String token = jwtService.generateToken(claims, signupUser);

                var authresponse = AuthenticationResponse.builder().message(
                        token
                     ).status(HttpStatus.ACCEPTED).build();

                return new ResponseEntity<AuthenticationResponse>(authresponse, HttpStatus.ACCEPTED);
            }
        } catch (Exception e) {
            e.printStackTrace();

            var authresponse = AuthenticationResponse.builder().message(
                    "Internal server error"
            ).status(HttpStatus.INTERNAL_SERVER_ERROR).build();

            return new ResponseEntity<AuthenticationResponse>(authresponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<AuthenticationResponse> login(Map<String, String> authenticateRequest) {
        log.info("inside login");

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticateRequest.get("email"),
                authenticateRequest.get("password")
        ));

        if (auth.isAuthenticated()) {
            User user = userRepository.findByEmail(authenticateRequest.get("email")).orElseThrow();

            if (user.getStatus().equalsIgnoreCase("true")) {
                String token = jwtService.generateToken(user);
                var authenticateResponse = AuthenticationResponse.builder()
                        .message(token)
                        .status(HttpStatus.ACCEPTED)
                        .build();


                return new ResponseEntity<AuthenticationResponse>(authenticateResponse, HttpStatus.ACCEPTED);

            } else {
                var authenticateResponse = AuthenticationResponse.builder()
                        .message("wait for admin approval")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            return    new ResponseEntity<AuthenticationResponse>(authenticateResponse, HttpStatus.BAD_REQUEST);
            }

        } else {

            var authenticateResponse = AuthenticationResponse.builder()
                    .message("Something went wrong")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        return   new ResponseEntity<AuthenticationResponse>(authenticateResponse, HttpStatus.INTERNAL_SERVER_ERROR);


        }

    }

}