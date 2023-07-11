package com.example.gamestore.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegisterDto {

    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;

    public UserRegisterDto() {
    }

    public UserRegisterDto(String email, String password, String confirmPassword, String fullName) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.fullName = fullName;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Size(min = 2)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
