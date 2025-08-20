package com.lohith.jwtSecurity.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {
    private String username;
    private String email;
    private Double monthlyLimit;
}
