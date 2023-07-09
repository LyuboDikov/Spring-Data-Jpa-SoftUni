package com.example.springdataintro.services.impl;

import com.example.springdataintro.models.entities.Author;
import com.example.springdataintro.models.entities.Book;
import com.example.springdataintro.repositories.AuthorRepository;
import com.example.springdataintro.services.AuthorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthorServiceImpl implements AuthorService {

    private static final String AUTHORS_FILE_PATH = "src/main/resources/files/authors.txt";
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void seedCategories() throws IOException {

        if (authorRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(AUTHORS_FILE_PATH))
                .forEach(row -> {
                    String[] fullName = row.split("\\s+");
                    Author author = new Author(fullName[0], fullName[1]);

                    authorRepository.save(author);
                });
    }

    @Override
    public Author getRandomAuthor() {

        long randomId = ThreadLocalRandom.current().nextLong(1, authorRepository.count() + 1);

        return authorRepository.findById(randomId).orElse(null);

    }

    @Override
    public List<String> getAllAuthorsOrderedByCountOfBooks() {

        return authorRepository.findAllByBooksSizeDESC()
                .stream()
                .map(author -> String.format("%s %s %d",
                        author.getFirstName(),
                        author.getLastName(),
                        author.getBooks().size())).toList();
    }

    @Override
    public List<String> findAuthorWhoseFirstNameEndsWithString(String endingString) {

        return authorRepository.findAllByFirstNameEndsWith(endingString)
                .stream()
                .map(author -> String.format("%s %s",
                        author.getFirstName(), author.getLastName()))
                .toList();
    }

    @Override
    public List<String> findAllAuthorsAndTheirTotalCopies() {

       return authorRepository.findAll()
                .stream()
                .map(author ->
                    String.format("%s %s - %d",
                            author.getFirstName(),
                            author.getLastName(),
                            author.getBooks()
                                    .stream()
                                    .mapToInt(Book::getCopies).sum()))
               .toList();
    }
}
