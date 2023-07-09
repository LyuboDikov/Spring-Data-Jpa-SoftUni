package com.example.springdataadvancedquering.repositories;

import com.example.springdataadvancedquering.models.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a ORDER BY size(a.books) DESC")
    List<Author> findAllByBooksSizeDESC();
    List<Author> findAllByFirstNameEndsWith(String endingString);
}
