package com.lohith.jwtSecurity.services;

import com.lohith.jwtSecurity.model.User;

public interface UserServices {

     boolean validateToken(String  token);
     User getUserById(Long id);
}
