package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * C端用户登录 DTO
 */
@Data
public class UserLoginDTO implements Serializable {
    // 微信授权码
    private String code;
}