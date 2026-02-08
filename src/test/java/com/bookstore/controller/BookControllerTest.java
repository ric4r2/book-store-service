package com.bookstore.controller;

import com.bookstore.dto.request.BookRequest;
import com.bookstore.dto.response.BookResponse;
import com.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookResponse bookResponse;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        bookResponse = new BookResponse();
        bookResponse.setId(1L);
        bookResponse.setName("Test Book");
        bookResponse.setAuthor("Test Author");
        bookResponse.setPrice(BigDecimal.valueOf(19.99));

        bookRequest = new BookRequest();
        bookRequest.setName("Test Book");
        bookRequest.setAuthor("Test Author");
        bookRequest.setPrice(BigDecimal.valueOf(19.99));
    }

    @Test
    @WithMockUser
    void getAllBooks_ShouldReturnListOfBooks() throws Exception {
        Mockito.when(bookService.getAllBooks()).thenReturn(List.of(bookResponse));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Test Book"));
    }

    @Test
    @WithMockUser
    void searchBooks_ShouldReturnPage() throws Exception {
        Page<BookResponse> page = new PageImpl<>(List.of(bookResponse));
        Mockito.when(bookService.getBooks(any(), anyInt(), anyInt(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/books/search")
                .param("search", "test")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Test Book"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void createBook_ShouldCreateBook() throws Exception {
        Mockito.when(bookService.createBook(any(BookRequest.class))).thenReturn(bookResponse);

        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Test Book"));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void createBook_ShouldReturnForbidden_ForClient() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isForbidden());
    }
}
