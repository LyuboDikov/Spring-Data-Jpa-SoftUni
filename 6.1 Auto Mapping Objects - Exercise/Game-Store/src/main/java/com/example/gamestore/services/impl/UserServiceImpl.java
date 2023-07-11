package com.example.gamestore.services.impl;

import com.example.gamestore.models.dtos.UserRegisterDto;
import com.example.gamestore.repositories.UserRepository;
import com.example.gamestore.services.UserService;
import com.example.gamestore.util.ValidationUtil;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        if(!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            System.out.println("Passwords must match!");
            return;
        }

        Set<ConstraintViolation<UserRegisterDto>> violations =
                validationUtil.violation(userRegisterDto);

        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }

        //TODO map DTO to Entity and save in DB
    }
}
