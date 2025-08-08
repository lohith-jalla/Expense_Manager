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

    @PostMapping("/setLimit/{limit}")
    public ResponseEntity<String> setLimit(
            @PathVariable("limit") Double limit,
            @RequestHeader("Authorization") String header
    ){
        if(header==null || !header.startsWith("Bearer ")){
            System.out.println("null token");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token =  header.substring(7);
        boolean isValid= userServiceImpl.validateToken(header);

        if(isValid){
            Boolean done= userServiceImpl.setLimit(token,limit);
            if (done) {
                return new  ResponseEntity<>("Limit Set Success", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/setMail")
    public ResponseEntity<String> setMail(
            @RequestParam("mail") String mail,
            @RequestHeader("Authorization") String header
    ){
        if(header==null || !header.startsWith("Bearer ")){
            System.out.println("null token");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token =  header.substring(7);
        boolean isValid= userServiceImpl.validateToken(header);

        if(isValid){
            Boolean done= userServiceImpl.setMail(token,mail);
            if (done) {
                return new  ResponseEntity<>("Email Set Success", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
