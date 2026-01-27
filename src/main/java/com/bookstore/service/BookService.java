package com.bookstore.service;

import com.bookstore.dto.request.BookRequest;
import com.bookstore.dto.response.BookResponse;
import com.bookstore.exception.ResourceAlreadyExistsException;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for book operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    /**
     * Get all books.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "'all'")
    public List<BookResponse> getAllBooks() {
        log.debug("Fetching all books");
        return bookRepository.findAllActive()
                .stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Get books with pagination and search.
     */
    @Transactional(readOnly = true)
    public Page<BookResponse> getBooks(String search, int page, int size, String sortBy, String sortDirection) {
        log.debug("Fetching books with search: '{}', page: {}, size: {}", search, page, size);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy != null ? sortBy : "name"));

        Page<Book> bookPage = bookRepository.findBooksWithSearch(search, pageable);
        return bookPage.map(book -> modelMapper.map(book, BookResponse.class));
    }

    /**
     * Get book by name.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "#name")
    public BookResponse getBookByName(String name) {
        log.debug("Fetching book by name: {}", name);
        Book book = bookRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "name", name));
        return modelMapper.map(book, BookResponse.class);
    }

    /**
     * Create a new book.
     */
    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public BookResponse createBook(BookRequest bookRequest) {
        log.debug("Creating new book: {}", bookRequest.getName());

        if (bookRepository.existsByName(bookRequest.getName())) {
            throw new ResourceAlreadyExistsException("Book", "name", bookRequest.getName());
        }

        Book book = modelMapper.map(bookRequest, Book.class);
        book = bookRepository.save(book);

        log.info("Book created successfully: {}", book.getName());
        return modelMapper.map(book, BookResponse.class);
    }

    /**
     * Update an existing book.
     */
    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public BookResponse updateBook(String name, BookRequest bookRequest) {
        log.debug("Updating book: {}", name);

        Book book = bookRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "name", name));

        if (!name.equals(bookRequest.getName()) && bookRepository.existsByName(bookRequest.getName())) {
            throw new ResourceAlreadyExistsException("Book", "name", bookRequest.getName());
        }

        modelMapper.map(bookRequest, book);
        book = bookRepository.save(book);

        log.info("Book updated successfully: {}", book.getName());
        return modelMapper.map(book, BookResponse.class);
    }

    /**
     * Delete a book (soft delete).
     */
    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public void deleteBook(String name) {
        log.debug("Deleting book: {}", name);

        Book book = bookRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "name", name));

        book.softDelete();
        bookRepository.save(book);

        log.info("Book soft deleted successfully: {}", name);
    }
}
