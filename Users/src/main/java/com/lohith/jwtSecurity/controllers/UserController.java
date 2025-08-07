package com.lohith.jwtSecurity.controllers;

import com.lohith.jwtSecurity.model.User;
import com.lohith.jwtSecurity.services.ServiceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id
    ) {
        boolean isValid= userServiceImpl.validateToken(token);

        if(isValid){
            User user= userServiceImpl.getUserById(id);
            return new  ResponseEntity<>(user, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getHashedPass/{id}")
    public ResponseEntity<String> getUserHashedPassword(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id
    ) {
        boolean isValid= userServiceImpl.validateToken(token);

        if(isValid){
            User user= userServiceImpl.getUserById(id);
            return new  ResponseEntity<>(user.getPassword(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
