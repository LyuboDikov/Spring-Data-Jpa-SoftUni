package com.example.springdataadvancedquering.services;

import com.example.springdataadvancedquering.models.entities.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {
    void seedCategories() throws IOException;
    Author getRandomAuthor();
    List<String> getAllAuthorsOrderedByCountOfBooks();
    List<String> findAuthorWhoseFirstNameEndsWithString(String endingString);
    List<String> findAllAuthorsAndTheirTotalCopies();
}
