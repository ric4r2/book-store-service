// Formatting test
package com.bookstore.controller;

import com.bookstore.dto.request.BookRequest;
import com.bookstore.dto.response.ApiResponse;
import com.bookstore.dto.response.BookResponse;
import com.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book management APIs")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

        private final BookService bookService;

        @Operation(summary = "Get all books", description = "Retrieve all active books")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved books", content = @Content(schema = @Schema(implementation = BookResponse.class)))
        })
        @GetMapping
        public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks() {
                List<BookResponse> books = bookService.getAllBooks();
                return ResponseEntity.ok(ApiResponse.success(books));
        }

        @Operation(summary = "Search books with pagination", description = "Search and filter books with pagination")
        @GetMapping("/search")
        public ResponseEntity<ApiResponse<Page<BookResponse>>> searchBooks(
                        @Parameter(description = "Search term") @RequestParam(required = false) String search,
                        @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
                        @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDirection) {
                Page<BookResponse> books = bookService.getBooks(search, page, size, sortBy, sortDirection);
                return ResponseEntity.ok(ApiResponse.success(books));
        }

        @Operation(summary = "Get book by name", description = "Retrieve a specific book by its name")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved book"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book not found")
        })
        @GetMapping("/{name}")
        public ResponseEntity<ApiResponse<BookResponse>> getBookByName(
                        @Parameter(description = "Book name") @PathVariable String name) {
                BookResponse book = bookService.getBookByName(name);
                return ResponseEntity.ok(ApiResponse.success(book));
        }

        @Operation(summary = "Create new book", description = "Add a new book to the store (Employee only)")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Book created successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Book already exists"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
        })
        @PostMapping
        @PreAuthorize("hasRole('EMPLOYEE')")
        public ResponseEntity<ApiResponse<BookResponse>> createBook(@Valid @RequestBody BookRequest bookRequest) {
                BookResponse book = bookService.createBook(bookRequest);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success("Book created successfully", book));
        }

        @Operation(summary = "Update book", description = "Update an existing book (Employee only)")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book updated successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book not found"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
        })
        @PutMapping("/{name}")
        @PreAuthorize("hasRole('EMPLOYEE')")
        public ResponseEntity<ApiResponse<BookResponse>> updateBook(
                        @Parameter(description = "Book name") @PathVariable String name,
                        @Valid @RequestBody BookRequest bookRequest) {
                BookResponse book = bookService.updateBook(name, bookRequest);
                return ResponseEntity.ok(ApiResponse.success("Book updated successfully", book));
        }

        @Operation(summary = "Delete book", description = "Soft delete a book (Employee only)")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book deleted successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book not found"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
        })
        @DeleteMapping("/{name}")
        @PreAuthorize("hasRole('EMPLOYEE')")
        public ResponseEntity<ApiResponse<Void>> deleteBook(
                        @Parameter(description = "Book name") @PathVariable String name) {
                bookService.deleteBook(name);
                return ResponseEntity.ok(ApiResponse.success("Book deleted successfully", null));
        }
}
