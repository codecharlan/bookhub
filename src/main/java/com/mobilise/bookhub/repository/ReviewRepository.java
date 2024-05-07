package com.mobilise.bookhub.repository;

import com.mobilise.bookhub.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
