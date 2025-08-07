package com.lohith.jwtSecurity.services;

import com.lohith.jwtSecurity.dto.LoginRequestDto;
import com.lohith.jwtSecurity.dto.LoginResponseDto;
import com.lohith.jwtSecurity.dto.SignUpResponseDto;

public interface AuthServices {
     LoginResponseDto login(LoginRequestDto req);
     SignUpResponseDto signUp(LoginRequestDto req);
}
