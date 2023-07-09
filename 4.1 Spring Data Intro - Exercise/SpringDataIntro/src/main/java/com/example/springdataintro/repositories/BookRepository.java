package com.example.springdataintro.repositories;

import com.example.springdataintro.models.entities.AgeRestriction;
import com.example.springdataintro.models.entities.Book;
import com.example.springdataintro.models.entities.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDate);
    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);
    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String authorFirstName, String authorLastName);
    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);
    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies);
    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lowerPrice, BigDecimal upperPrice);
    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate lower, LocalDate upper);
    List<Book> findAllByTitleContainsIgnoreCase(String title);
    List<Book> findAllByAuthor_LastNameStartsWith(String startString);
    @Query("SELECT COUNT(b) FROM Book b WHERE length(b.title) > :length")
    int booksCountWithTitleLengthMoreThan(@Param(value="length") int titleLength);
    List<Book> findAllByTitle(String title);

    @Modifying
    @Query("UPDATE Book b SET b.copies = b.copies + :copies WHERE b.releaseDate > :date")
    int updateCopiesByReleaseDate(@Param(value = "copies") int copies,
                                  @Param(value = "date") LocalDate date);
}
