package com.example.springdataintro;

import com.example.springdataintro.models.entities.AgeRestriction;
import com.example.springdataintro.models.entities.Book;
import com.example.springdataintro.services.AuthorService;
import com.example.springdataintro.services.BookService;
import com.example.springdataintro.services.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService,
                                 BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

//        System.out.println("Task 1:");
//        printAllBooksAfterYear(2000);
//        separateTasksResults();
//
//        System.out.println("Task 2:");
//        printAllAuthorsNamesWhoseBooksAreReleasedBeforeYear(1990);
//        separateTasksResults();
//
//        System.out.println("Task 3:");
//        printAllAuthorsNamesAndNumberOfTheirBooks();
//        separateTasksResults();
//
//        System.out.println("Task 4:");
//        printAllBooksByAuthorNameOrderedByReleaseDate("George", "Powell");

        System.out.println("Please select exercise:");
        int exerciseNum = Integer.parseInt(bufferedReader.readLine());

        switch (exerciseNum) {
            case 1 -> bookTitlesByAgeRestriction();
            case 2 -> goldenBooks();
            case 3 -> booksByPrice();
            case 4 -> notReleasedBooks();
            case 5 -> booksReleasedBeforeDate();
            case 6 -> authorsSearch();
            case 7 -> booksSearch();
            case 8 -> bookTitlesSearch();
            case 9 -> countBooks();
            case 10 -> totalBookCopies();
            case 11 -> reducedBook();
            case 12 -> increaseBookCopies();
        }
    }

    private void increaseBookCopies() throws IOException {

        System.out.println("Please enter release date:");
        LocalDate date = LocalDate.parse(bufferedReader.readLine(),
                DateTimeFormatter.ofPattern("dd MMM yyyy").withLocale(Locale.ENGLISH));

        System.out.println("Please enter copies count to add to the books:");
        int copies = Integer.parseInt(bufferedReader.readLine());

        System.out.println(bookService.increaseBookCopiesForBooksReleasedAfterDate(date, copies));
    }

    private void reducedBook() throws IOException {
        System.out.println("Please enter book title and you'll get " +
                "info about the edition type, age restriction and price:");

        String title = bufferedReader.readLine();

        bookService.findBookByTitle(title)
                .forEach(System.out::println);
    }

    private void totalBookCopies() {
        authorService.findAllAuthorsAndTheirTotalCopies()
                .forEach(System.out::println);
    }

    private void countBooks() throws IOException {
        System.out.println("Please enter a number to check how many books we have, with title longer than the given number:");

        int length = Integer.parseInt(bufferedReader.readLine());

        System.out.println(bookService.findAllBooksWithTitleLongerThanTheGivenSymbolsLength(length));
    }

    private void bookTitlesSearch() throws IOException {
        System.out.println("Please enter text to check if there's any books, with authors whose last name starts with the text:");

        String text = bufferedReader.readLine();

        bookService.findAllBooksWrittenByAuthorWithLastNameStartingWith(text)
                .forEach(System.out::println);
    }

    private void booksSearch() throws IOException {
        System.out.println("Please enter text(case insensitive) to check if there's any book title that contains the text: ");

        String text = bufferedReader.readLine();

        bookService.findAllBooksWithTitleContainingString(text)
                .forEach(System.out::println);
    }

    private void authorsSearch() throws IOException {
        System.out.println("Please enter first name ending text:");

        String endingString = bufferedReader.readLine();

        authorService.findAuthorWhoseFirstNameEndsWithString(endingString)
                .forEach(System.out::println);
    }

    private void booksReleasedBeforeDate() throws IOException {
        System.out.println("Please enter date in format dd-MM-yyyy :");

        LocalDate date = LocalDate.parse(bufferedReader.readLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        bookService.findAllBooksReleasedBeforeDate(date)
                .forEach(System.out::println);

    }

    private void notReleasedBooks() throws IOException {
        System.out.println("Please enter year:");
        int year = Integer.parseInt(bufferedReader.readLine());

        bookService.findAllBooksNotReleasedInYear(year)
                .forEach(System.out::println);
    }

    private void booksByPrice() {
        bookService.findAllBookTitlesWithBookPriceLessThan5AndMoreThan40()
                .forEach(System.out::println);
    }

    private void goldenBooks() {

        bookService.findAllGoldBookTitlesWithCopiesLessThan5000()
                .forEach(System.out::println);
    }

    private void bookTitlesByAgeRestriction() throws IOException {
        System.out.println("Please enter age restriction:");

        AgeRestriction ageRestriction =
                AgeRestriction.valueOf(bufferedReader.readLine().toUpperCase());

        bookService.findAllBookTitlesWithAgeRestriction(ageRestriction)
                .forEach(System.out::println);
    }

    private void separateTasksResults() {
        System.out.println();
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();
        System.out.println();
    }

    private void printAllBooksByAuthorNameOrderedByReleaseDate(String firstName, String lastName) {
        bookService.findAllBooksByAuthorFirstAndLastNameOrderedByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesAndNumberOfTheirBooks() {
        authorService.getAllAuthorsOrderedByCountOfBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWhoseBooksAreReleasedBeforeYear(int year) {
        bookService.findAllAuthorsWithBooksReleasedBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService.findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedCategories();
        bookService.seedBooks();
    }
}
