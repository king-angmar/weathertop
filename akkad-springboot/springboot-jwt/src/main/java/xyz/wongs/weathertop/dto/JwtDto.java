package xyz.wongs.weathertop.dto;

import lombok.Data;

@Data
public class JwtDto {
    // 加密方式
    private String alg;

    // token
    private String token;
}
