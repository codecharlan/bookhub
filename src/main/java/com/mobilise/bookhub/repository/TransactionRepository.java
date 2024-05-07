package com.mobilise.bookhub.repository;

import com.mobilise.bookhub.entity.Transaction;
import com.mobilise.bookhub.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long > {
    Optional<Transaction> findTransactionByUserIdAndBookIdAndType(Long userId, Long bookId, TransactionType type);
}
