package com.mobilise.bookhub.entity;

import com.mobilise.bookhub.enums.TransactionStatus;
import com.mobilise.bookhub.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction entity class representing a transaction between a user and a book.
 *
 * @author codecharlan
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Transaction {
    /**
     * The unique identifier for the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who is involved in the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The book that is involved in the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    /**
     * The amount of the transaction.
     */
    private BigDecimal amount;

    /**
     * The type of the transaction.
     */
    private TransactionType type;

    /**
     * The date and time when the transaction was created.
     */
    @CreatedDate
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss")
    private LocalDateTime transactionDate;

    /**
     * The status of the transaction.
     */
    private TransactionStatus status;
}
