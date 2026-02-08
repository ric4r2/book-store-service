package com.bookstore.dto;

import com.bookstore.dto.request.LoginRequest;
import com.bookstore.dto.request.RegisterRequest;
import com.bookstore.dto.response.ApiResponse;
import com.bookstore.dto.response.BookResponse;
import com.bookstore.dto.response.JwtResponse;
import com.bookstore.exception.ErrorResponse;
import com.bookstore.model.enums.AgeGroup;
import com.bookstore.model.enums.Language;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DtoCoverageTest {

    @Test
    void testBookResponse() {
        BookResponse book = new BookResponse();
        book.setId(1L);
        book.setName("Title");
        book.setAuthor("Author");
        book.setGenre("Genre");
        book.setPrice(BigDecimal.TEN);
        book.setPublicationYear(LocalDate.of(2020, 1, 1));
        book.setPages(100);
        book.setLanguage(Language.ENGLISH);
        book.setAgeGroup(AgeGroup.ADULT);
        book.setCharacteristics("Char");
        book.setDescription("Desc");
        book.setCreatedAt(null);
        book.setUpdatedAt(null);

        assertEquals(1L, book.getId());
        assertEquals("Title", book.getName());
        assertEquals("Author", book.getAuthor());
        assertEquals("Genre", book.getGenre());
        assertEquals(BigDecimal.TEN, book.getPrice());
        assertEquals(LocalDate.of(2020, 1, 1), book.getPublicationYear());
        assertEquals(100, book.getPages());
        assertEquals(Language.ENGLISH, book.getLanguage());
        assertEquals(AgeGroup.ADULT, book.getAgeGroup());
        assertEquals("Char", book.getCharacteristics());
        assertEquals("Desc", book.getDescription());
        assertNull(book.getCreatedAt());
        assertNull(book.getUpdatedAt());

        BookResponse book2 = new BookResponse(1L, "Title", "Genre", AgeGroup.ADULT, BigDecimal.TEN,
                LocalDate.of(2020, 1, 1), "Author", 100, "Char", "Desc", Language.ENGLISH, null, null);
        assertEquals(book, book2);
        assertEquals(book.hashCode(), book2.hashCode());
        assertNotNull(book.toString());
    }

    @Test
    void testJwtResponse() {
        JwtResponse response = new JwtResponse("access", "refresh", "email@test.com", "CLIENT");

        JwtResponse response2 = new JwtResponse();
        response2.setAccessToken("access");
        response2.setRefreshToken("refresh");
        response2.setEmail("email@test.com");
        response2.setRole("CLIENT");
        // tokenType has default value "Bearer"
        response2.setTokenType("Bearer");

        // The custom constructor in JwtResponse does NOT set tokenType, it relies on
        // default field init?
        // Let's check JwtResponse.java again.
        // private String tokenType = "Bearer";
        // Constructor: this.accessToken = ...;
        // So tokenType remains "Bearer".

        assertEquals(response.getAccessToken(), response2.getAccessToken());
        assertEquals(response.getRefreshToken(), response2.getRefreshToken());
        assertEquals(response.getEmail(), response2.getEmail());
        assertEquals(response.getRole(), response2.getRole());
        assertEquals(response.getTokenType(), response2.getTokenType());

        assertEquals(response, response2);
        assertEquals(response.hashCode(), response2.hashCode());
        assertNotNull(response.toString());
    }

    @Test
    void testErrorResponse() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse error = new ErrorResponse(now, 400, "Bad Request", "Message", "/path");

        ErrorResponse error2 = new ErrorResponse();
        error2.setTimestamp(now);
        error2.setStatus(400);
        error2.setError("Bad Request");
        error2.setMessage("Message");
        error2.setPath("/path");

        // The 5-arg constructor leaves validationErrors null?
        // Let's check ErrorResponse.java. Yes.

        // error2 has null validationErrors by default.

        assertEquals(error, error2);
        assertEquals(error.hashCode(), error2.hashCode());
        assertNotNull(error.toString());
    }

    @Test
    void testRegisterRequest() {
        RegisterRequest request = new RegisterRequest("Name", "email@test.com", "password", "123456", "Address");

        RegisterRequest request2 = new RegisterRequest();
        request2.setName("Name");
        request2.setEmail("email@test.com");
        request2.setPassword("password");
        request2.setPhone("123456");
        request2.setAddress("Address");

        assertEquals(request, request2);
        assertEquals(request.hashCode(), request2.hashCode());
        assertNotNull(request.toString());
    }

    @Test
    void testLoginRequest() {
        LoginRequest request = new LoginRequest("email@test.com", "password");

        LoginRequest request2 = new LoginRequest();
        request2.setEmail("email@test.com");
        request2.setPassword("password");

        assertEquals(request, request2);
        assertEquals(request.hashCode(), request2.hashCode());
        assertNotNull(request.toString());
    }

    @Test
    void testApiResponse() {
        ApiResponse<String> response = new ApiResponse<>(true, "Message", "Data");

        ApiResponse<String> response2 = new ApiResponse<>();
        response2.setSuccess(true);
        response2.setMessage("Message");
        response2.setData("Data");

        assertEquals(response.getSuccess(), response2.getSuccess());
        assertEquals(response.getMessage(), response2.getMessage());
        assertEquals(response.getData(), response2.getData());

        // ApiResponse might not have equals/hashCode if @Data is not present or it is
        // generic.
        // Assuming @Data is present as per recent trends in this codebase.
        // If not, we skip equals check.
        // Step 532 showed high missed instructions for ApiResponse, likely @Data
        // methods.
        // So checking equals/hashCode covers them.

        assertEquals(response, response2);
        assertEquals(response.hashCode(), response2.hashCode());
        assertNotNull(response.toString());
    }
}
