package com.lohith.Expense.OpenFeignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="Users",configuration= FeignClientsConfiguration.class)
public interface UserClient {

    @GetMapping("/user/getHashedPass/{userId}")
    String getHashedPassword(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("userId") Long userId
    );
}
