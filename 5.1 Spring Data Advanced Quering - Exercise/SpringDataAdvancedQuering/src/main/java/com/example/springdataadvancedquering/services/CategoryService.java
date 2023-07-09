package com.example.springdataadvancedquering.services;

import com.example.springdataadvancedquering.models.entities.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> getRandomCategories();
}
