package com.bookstore.dto.response;

import com.bookstore.model.enums.AgeGroup;
import com.bookstore.model.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Book DTO for responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Book information")
public class BookResponse {

    @Schema(description = "Book ID", example = "1")
    private Long id;

    @Schema(description = "Book name", example = "The Great Gatsby")
    private String name;

    @Schema(description = "Book genre", example = "Classic Fiction")
    private String genre;

    @Schema(description = "Age group", example = "ADULT")
    private AgeGroup ageGroup;

    @Schema(description = "Book price", example = "12.99")
    private BigDecimal price;

    @Schema(description = "Publication date", example = "1925-04-10")
    private LocalDate publicationYear;

    @Schema(description = "Author name", example = "F. Scott Fitzgerald")
    private String author;

    @Schema(description = "Number of pages", example = "180")
    private Integer pages;

    @Schema(description = "Book characteristics", example = "Hardcover, First Edition Style")
    private String characteristics;

    @Schema(description = "Book description", example = "A story of decadence and excess in the Jazz Age")
    private String description;

    @Schema(description = "Book language", example = "ENGLISH")
    private Language language;

    @Schema(description = "Created timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated timestamp")
    private LocalDateTime updatedAt;
}
