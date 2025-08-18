package com.lohith.jwtSecurity.controllers;


import com.lohith.jwtSecurity.dto.LoginRequestDto;
import com.lohith.jwtSecurity.dto.LoginResponseDto;
import com.lohith.jwtSecurity.dto.SignUpResponseDto;
import com.lohith.jwtSecurity.services.ServiceImpl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto req
    ) {
        return ResponseEntity.ok(authServiceImpl.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<SignUpResponseDto> Signup(
            @RequestBody LoginRequestDto req
    ) {
        SignUpResponseDto res= authServiceImpl.signUp(req);
        return ResponseEntity.ok(res);
    }


}
