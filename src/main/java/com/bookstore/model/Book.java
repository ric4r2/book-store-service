package com.bookstore.model;

import com.bookstore.model.base.BaseEntity;
import com.bookstore.model.enums.AgeGroup;
import com.bookstore.model.enums.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Book entity representing books available in the store.
 */
@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_books_name", columnList = "name"),
        @Index(name = "idx_books_author", columnList = "author"),
        @Index(name = "idx_books_genre", columnList = "genre")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    private String genre;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group")
    private AgeGroup ageGroup;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "publication_year")
    private LocalDate publicationYear;

    private String author;

    @Column(name = "number_of_pages")
    private Integer pages;

    @Column(columnDefinition = "TEXT")
    private String characteristics;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Language language;
}
