package com.lohith.jwtSecurity.controllers;


import com.lohith.jwtSecurity.config.AuthUtil;
import com.lohith.jwtSecurity.dto.LoginRequestDto;
import com.lohith.jwtSecurity.dto.LoginResponseDto;
import com.lohith.jwtSecurity.dto.SignUpResponseDto;
import com.lohith.jwtSecurity.model.User;
import com.lohith.jwtSecurity.repo.UserRepo;
import com.lohith.jwtSecurity.services.ServiceImpl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;
    private final AuthUtil authUtil;
    private final UserRepo  userRepo;

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
