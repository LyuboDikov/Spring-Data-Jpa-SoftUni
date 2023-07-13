package com.example.gamestore.services;

import com.example.gamestore.models.dtos.GameAddDto;

public interface GameService {
    void addGame(GameAddDto gameAddDto);
}
