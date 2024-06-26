package com.mobilise.bookhub.repository;

import com.mobilise.bookhub.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByName(String publisherName);
}
