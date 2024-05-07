package com.mobilise.bookhub.repository;

import com.mobilise.bookhub.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleAndAuthor_Name(String title, String authorsName);
    Page<Book> findByTitleContainingIgnoreCase(String searchTerm, Pageable pageable);
    Page<Book> findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCase(String searchTerm, String searchTerm1, Pageable pageable);
}
