package com.bookstore.repository;

import com.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Book entity.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByName(String name);

    Boolean existsByName(String name);

    @Query("SELECT b FROM Book b WHERE b.deletedAt IS NULL")
    List<Book> findAllActive();

    @Query("SELECT b FROM Book b WHERE b.deletedAt IS NULL AND " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.genre) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Book> findBooksWithSearch(@Param("search") String search, Pageable pageable);
}
