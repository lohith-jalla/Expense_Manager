package com.lohith.Expense.Filter;

import com.lohith.Expense.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String authHeader=req.getHeader("Authorization");

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            log.warn("No token found in request");
            filterChain.doFilter(req,res);
            return;
        }
        String token=authHeader.substring(7);

        String username=jwtService.extractUsername(token);

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null ){
            if(jwtService.validate(token)){
                UsernamePasswordAuthenticationToken upToken=new UsernamePasswordAuthenticationToken(username,null,new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(upToken);
                System.out.println("JWT Validated for user: " + username);
            }
        }
        filterChain.doFilter(req,res);
    }
}
