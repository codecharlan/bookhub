package com.mobilise.bookhub.repository;

import com.mobilise.bookhub.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByEmailAddress(String email);
}