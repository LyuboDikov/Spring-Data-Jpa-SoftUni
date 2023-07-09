package com.example.springdataadvancedquering.services;

import com.example.springdataadvancedquering.models.entities.AgeRestriction;
import com.example.springdataadvancedquering.models.entities.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksReleasedBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderedByReleaseDate(String firstName, String lastName);

    List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction);

    List<String> findAllGoldBookTitlesWithCopiesLessThan5000();

    List<String> findAllBookTitlesWithBookPriceLessThan5AndMoreThan40();

    List<String> findAllBooksNotReleasedInYear(int year);

    List<String> findAllBooksReleasedBeforeDate(LocalDate date);

    List<String> findAllBooksWithTitleContainingString(String text);

    List<String> findAllBooksWrittenByAuthorWithLastNameStartingWith(String text);

    int findAllBooksWithTitleLongerThanTheGivenSymbolsLength(int length);

    List<String> findBookByTitle(String title);

    int increaseBookCopiesForBooksReleasedAfterDate(LocalDate date, int copies);
}
