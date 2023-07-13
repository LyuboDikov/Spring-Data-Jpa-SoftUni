package com.example.gamestore.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class UserLoginDto {

    private String email;
    private String password;

    public UserLoginDto() {
    }

    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Email(message = "Please enter valid email!")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{6,}).*$",
            message = "Password must contain at least 1 Upper case, 1 Lower case, 1 Digit and to be at least 6 symbols long!")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
