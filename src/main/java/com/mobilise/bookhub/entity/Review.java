package com.mobilise.bookhub.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Represents a review of a book.
 *
 * @author codecharlan
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {
    /**
     * The unique identifier for the review.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who wrote the review.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The book being reviewed.
     */
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    /**
     * The rating of the book, between 1 and 5.
     *
     */
    @NotNull(message = "Book rating is required")
    @Min(1)
    @Max(5)
    private int rating;

    /**
     * The comments left by the user about the book.
     *
     */
    @Size(max = 455)
    private String comments;

    /**
     * The date and time when the review was created.
     *
     */
    @UpdateTimestamp
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss")
    private LocalDateTime reviewDate;
}
