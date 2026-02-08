package com.bookstore.service;

import com.bookstore.dto.request.BookRequest;
import com.bookstore.dto.response.BookResponse;
import com.bookstore.exception.ResourceAlreadyExistsException;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.model.Book;
import com.bookstore.model.enums.AgeGroup;
import com.bookstore.model.enums.Language;
import com.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookRequest bookRequest;
    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setAgeGroup(AgeGroup.ADULT);
        book.setLanguage(Language.ENGLISH);

        bookRequest = new BookRequest();
        bookRequest.setName("Test Book");
        bookRequest.setAuthor("Test Author");
        bookRequest.setPrice(BigDecimal.valueOf(19.99));
        bookRequest.setAgeGroup(AgeGroup.ADULT);
        bookRequest.setLanguage(Language.ENGLISH);

        bookResponse = new BookResponse();
        bookResponse.setId(1L);
        bookResponse.setName("Test Book");
        bookResponse.setAuthor("Test Author");
        bookResponse.setPrice(BigDecimal.valueOf(19.99));
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        when(bookRepository.findAllActive()).thenReturn(Collections.singletonList(book));
        when(modelMapper.map(any(Book.class), eq(BookResponse.class))).thenReturn(bookResponse);

        List<BookResponse> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookResponse.getName(), result.get(0).getName());
        verify(bookRepository).findAllActive();
    }

    @Test
    void getBooks_ShouldReturnPagedBooks() {
        Page<Book> bookPage = new PageImpl<>(Collections.singletonList(book));
        when(bookRepository.findBooksWithSearch(any(), any(Pageable.class))).thenReturn(bookPage);
        when(modelMapper.map(any(Book.class), eq(BookResponse.class))).thenReturn(bookResponse);

        Page<BookResponse> result = bookService.getBooks("test", 0, 10, "name", "asc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findBooksWithSearch(any(), any(Pageable.class));
    }

    @Test
    void getBookByName_ShouldReturnBook_WhenExists() {
        when(bookRepository.findByName("Test Book")).thenReturn(Optional.of(book));
        when(modelMapper.map(any(Book.class), eq(BookResponse.class))).thenReturn(bookResponse);

        BookResponse result = bookService.getBookByName("Test Book");

        assertNotNull(result);
        assertEquals("Test Book", result.getName());
    }

    @Test
    void getBookByName_ShouldThrowException_WhenNotFound() {
        when(bookRepository.findByName("Non Existent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookByName("Non Existent"));
    }

    @Test
    void createBook_ShouldReturnCreatedBook() {
        when(bookRepository.existsByName(bookRequest.getName())).thenReturn(false);
        when(modelMapper.map(bookRequest, Book.class)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(book, BookResponse.class)).thenReturn(bookResponse);

        BookResponse result = bookService.createBook(bookRequest);

        assertNotNull(result);
        assertEquals("Test Book", result.getName());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_ShouldThrowException_WhenBookExists() {
        when(bookRepository.existsByName(bookRequest.getName())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> bookService.createBook(bookRequest));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() {
        when(bookRepository.findByName("Test Book")).thenReturn(Optional.of(book));
        doNothing().when(modelMapper).map(any(BookRequest.class), any(Book.class));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(book, BookResponse.class)).thenReturn(bookResponse);

        BookResponse result = bookService.updateBook("Test Book", bookRequest);

        assertNotNull(result);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void deleteBook_ShouldSoftDelete() {
        when(bookRepository.findByName("Test Book")).thenReturn(Optional.of(book));

        bookService.deleteBook("Test Book");

        verify(bookRepository).save(book);
        assertTrue(book.isDeleted());
    }
}
