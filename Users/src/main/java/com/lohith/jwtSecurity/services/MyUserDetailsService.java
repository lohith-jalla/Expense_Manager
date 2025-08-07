package com.lohith.jwtSecurity.services;

import com.lohith.jwtSecurity.model.User;
import com.lohith.jwtSecurity.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo repo;

    public MyUserDetailsService(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=repo.findByUserName(username).orElse(null);

        if(user==null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }
}
