package com.example.models.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {

    public String getJwtToken() {
        return jwtToken;
    }

    private String jwtToken;


}
