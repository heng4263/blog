package com.example.kkblog.controller.dto;

import lombok.Data;

/**
 * @Author Hyh
 * @Date 2024 04 17 20 26
 **/
@Data
public class PasswordDto {
    private String oldPassword;

    private String newPassword;

    private String confirmPassword;
}
