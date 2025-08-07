package com.lohith.jwtSecurity.services.ServiceImpl;


import com.lohith.jwtSecurity.config.AuthUtil;
import com.lohith.jwtSecurity.model.User;
import com.lohith.jwtSecurity.repo.UserRepo;
import com.lohith.jwtSecurity.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServices {

    private final AuthUtil authUtil;
    private final UserRepo userRepo;

    public boolean validateToken(String  token) {
        token=token.substring(7);
        boolean isValid=authUtil.validate(token);

        return isValid;
    }

    public User getUserById(Long id){
        return userRepo.findById(id).orElse(null);
    }


}
