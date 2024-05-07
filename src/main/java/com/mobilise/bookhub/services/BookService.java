package com.mobilise.bookhub.services;


import com.mobilise.bookhub.dto.request.BookRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.BookResponseDto;

import java.util.List;

public interface BookService {
    ApiResponse<BookResponseDto> createBook(String email, BookRequestDto newBook);

    ApiResponse<BookResponseDto> editBook(String email, Long id, BookRequestDto updatedBook);

    ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>> getAllBooks(String email,
                                                                        int pageNumber,
                                                                        int pageSize,
                                                                        String sortBy,
                                                                        String sortOrder,
                                                                        String searchTerm);

    ApiResponse<BookResponseDto> getBookById(String email, Long id);

    ApiResponse<String> deleteBook(String email, Long id);

    ApiResponse<BookResponseDto> borrowBook(Long bookId, String email, Integer borrowCount);

    ApiResponse<BookResponseDto> returnBook(String email, Long bookId, int returnCount);

    ApiResponse<BookResponseDto> purchaseBook(Long bookId, String email, Integer borrowCount);

    ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>> searchBooksByTitleOrAuthor(String email, int pageNumber,
                                                                                       int pageSize, String searchTerm);
}
