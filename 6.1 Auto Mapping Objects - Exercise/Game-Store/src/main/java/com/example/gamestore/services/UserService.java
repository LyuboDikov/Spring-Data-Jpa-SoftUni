package com.example.gamestore.services;

import com.example.gamestore.models.dtos.UserLoginDto;
import com.example.gamestore.models.dtos.UserRegisterDto;

public interface UserService {
    void registerUser(UserRegisterDto userRegisterDto);

    void loginUser(UserLoginDto userLoginDto);
}
