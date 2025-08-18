package com.lohith.jwtSecurity.services.ServiceImpl;


import com.lohith.jwtSecurity.config.AuthUtil;
import com.lohith.jwtSecurity.dto.LoginRequestDto;
import com.lohith.jwtSecurity.dto.LoginResponseDto;
import com.lohith.jwtSecurity.dto.SignUpResponseDto;
import com.lohith.jwtSecurity.model.User;
import com.lohith.jwtSecurity.repo.UserRepo;
import com.lohith.jwtSecurity.services.AuthServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthServices {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto req) {

        Authentication auth=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        User user= (User) auth.getPrincipal();
        String token=authUtil.generateAccessToken(user);

        return new LoginResponseDto(token,user.getId());
    }

    public SignUpResponseDto signUp(LoginRequestDto req) {
        User user=repo.findByUserName(req.getUsername()).orElse(null);
        if(user!=null){
            throw new IllegalArgumentException("User already exists");
        }

        user = repo.save(User
                .builder()
                .userName(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .build()
        );

        return new SignUpResponseDto(user.getId(),user.getUsername(),user.getEmail());
    }
}
