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

    @Override
    public boolean setLimit(String token, Double limit) {
        String username=authUtil.getUserNameFromToken(token);
        User user=userRepo.findByUserName(username).orElse(null);
        System.out.println(user.getUsername());

        if(user==null){
            return false;
        }
        user.setMonthlyLimit(limit);
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean setMail(String token, String mail) {
        String username=authUtil.getUserNameFromToken(token);
        User user=userRepo.findByUserName(username).orElse(null);
        if(user==null){
            return false;
        }
        user.setEmail(mail);
        userRepo.save(user);
        return true;
    }

    @Override
    public Long getUserId(String token) {
        token = token.substring(7);
        return authUtil.getUserIdFromToken(token);
    }

}
