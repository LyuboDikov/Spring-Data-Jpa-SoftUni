package com.example.springdataintro.services.impl;

import com.example.springdataintro.models.entities.*;
import com.example.springdataintro.repositories.BookRepository;
import com.example.springdataintro.services.AuthorService;
import com.example.springdataintro.services.BookService;
import com.example.springdataintro.services.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final String BOOKS_FILE_PATH = "src/main/resources/files/books.txt";
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (bookRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(BOOKS_FILE_PATH))
                .forEach(row -> {
                    String[] bookInfo = row.split("\\s+");

                    Book book = createBookFromFileInfo(bookInfo);

                    bookRepository.save(book);
                });
    }

    @Override
    public List<Book> findAllBooksAfterYear(int year) {
        return bookRepository.findAllByReleaseDateAfter(LocalDate.of(year, 12,31));
    }

    @Override
    public List<String> findAllAuthorsWithBooksReleasedBeforeYear(int year) {
        return bookRepository
                .findAllByReleaseDateBefore(LocalDate.of(year, 1, 1))
                .stream().map(book -> String.format("%s %s",
                        book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .distinct().toList();
    }

    @Override
    public List<String> findAllBooksByAuthorFirstAndLastNameOrderedByReleaseDate(String firstName, String lastName) {

       return bookRepository
                .findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName, lastName)
               .stream().map(book -> String.format("%s %s %d",
                       book.getTitle(),
                       book.getReleaseDate(),
                       book.getCopies())).toList();
    }

    @Override
    public List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction) {

        return bookRepository.findAllByAgeRestriction(ageRestriction)
                .stream()
                .map(Book::getTitle)
                .toList();
    }

    @Override
    public List<String> findAllGoldBookTitlesWithCopiesLessThan5000() {

        return bookRepository.findAllByEditionTypeAndCopiesLessThan(EditionType.GOLD, 5000)
                .stream()
                .map(Book::getTitle)
                .toList();
    }

    @Override
    public List<String> findAllBookTitlesWithBookPriceLessThan5AndMoreThan40() {

        return bookRepository.
                findAllByPriceLessThanOrPriceGreaterThan(
                        BigDecimal.valueOf(5L), BigDecimal.valueOf(40L))
                .stream()
                .map(book -> String.format("%s - $%.2f",
                        book.getTitle(), book.getPrice()))
                .toList();
    }

    @Override
    public List<String> findAllBooksNotReleasedInYear(int year) {

        LocalDate lower = LocalDate.of(year, 1, 1);
        LocalDate upper = LocalDate.of(year, 12, 31);

        return bookRepository.findAllByReleaseDateBeforeOrReleaseDateAfter(lower, upper)
                .stream()
                .map(Book::getTitle)
                .toList();
    }

    @Override
    public List<String> findAllBooksReleasedBeforeDate(LocalDate date) {

        return bookRepository.findAllByReleaseDateBefore(date)
                .stream()
                .map(book -> String.format("%s %s %.2f",
                        book.getTitle(), book.getEditionType().name(), book.getPrice()))
                .toList();
    }

    @Override
    public List<String> findAllBooksWithTitleContainingString(String text) {

        return bookRepository.findAllByTitleContainsIgnoreCase(text)
                .stream()
                .map(Book::getTitle)
                .toList();
    }

    @Override
    public List<String> findAllBooksWrittenByAuthorWithLastNameStartingWith(String text) {

        return bookRepository.findAllByAuthor_LastNameStartsWith(text)
                .stream()
                .map(book -> String.format("%s (%s %s)",
                        book.getTitle(),
                        book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .toList();
    }

    @Override
    public int findAllBooksWithTitleLongerThanTheGivenSymbolsLength(int length) {

        return bookRepository.booksCountWithTitleLengthMoreThan(length);
    }

    @Override
    public List<String> findBookByTitle(String title) {

        return bookRepository.findAllByTitle(title)
                .stream()
                .map(book -> String.format("%s %s %s %.2f",
                        book.getTitle(),
                        book.getEditionType().name(),
                        book.getAgeRestriction().name(),
                        book.getPrice()))
                .toList();
    }

    @Transactional
    @Override
    public int increaseBookCopiesForBooksReleasedAfterDate(LocalDate date, int copies) {

        int affectedRows = bookRepository.updateCopiesByReleaseDate(copies, date);

        return affectedRows * copies;
    }

    private Book createBookFromFileInfo(String[] bookInfo) {
        EditionType editionType =
                EditionType.values()[Integer.parseInt(bookInfo[0])];

        LocalDate releaseDate =
                LocalDate.parse(bookInfo[1], DateTimeFormatter.ofPattern("d/M/yyyy"));

        Integer copies =
                Integer.parseInt(bookInfo[2]);

        BigDecimal price =
                new BigDecimal(bookInfo[3]);

        AgeRestriction ageRestriction =
                AgeRestriction.values()[Integer.parseInt(bookInfo[4])];

        String title =
                Arrays.stream(bookInfo)
                        .skip(5).collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();

        Set<Category> categories = categoryService.getRandomCategories();

        return new Book(editionType, releaseDate, copies, price,
                ageRestriction, title, author, categories);
    }
}
