package com.bookstore.dto.request;

import com.bookstore.model.enums.AgeGroup;
import com.bookstore.model.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Book DTO for requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Book information")
public class BookRequest {

    @NotBlank(message = "Book name is required")
    @Size(min = 1, max = 255, message = "Book name must be between 1 and 255 characters")
    @Schema(description = "Book name", example = "The Great Gatsby")
    private String name;

    @Schema(description = "Book genre", example = "Classic Fiction")
    private String genre;

    @Schema(description = "Age group", example = "ADULT")
    private AgeGroup ageGroup;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Schema(description = "Book price", example = "12.99")
    private BigDecimal price;

    @Schema(description = "Publication date", example = "1925-04-10")
    private LocalDate publicationYear;

    @NotBlank(message = "Author is required")
    @Schema(description = "Author name", example = "F. Scott Fitzgerald")
    private String author;

    @Min(value = 1, message = "Number of pages must be at least 1")
    @Schema(description = "Number of pages", example = "180")
    private Integer pages;

    @Schema(description = "Book characteristics", example = "Hardcover, First Edition Style")
    private String characteristics;

    @Schema(description = "Book description", example = "A story of decadence and excess in the Jazz Age")
    private String description;

    @Schema(description = "Book language", example = "ENGLISH")
    private Language language;
}
