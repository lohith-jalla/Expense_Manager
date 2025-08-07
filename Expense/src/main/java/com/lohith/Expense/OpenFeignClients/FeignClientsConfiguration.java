package com.lohith.Expense.OpenFeignClients;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignClientsConfiguration {

    @Bean
    // This intercepts the request and adds the Jwt token to it as it goes to the user microservice
    // to collect some data
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            String token =getCurrentToken();
            if(token!=null){
                requestTemplate.header("Authorization",token);
            }
        };
    }


    // The SecurityContextHolder gets you the current JWT token
    private String getCurrentToken() {
        // Get the token from SecurityContext
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getCredentials() instanceof String token) {
            return "Bearer " + token;
        }
        return null;
    }
}
