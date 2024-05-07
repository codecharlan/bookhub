package com.mobilise.bookhub.utils;

import com.mobilise.bookhub.entity.Book;
import com.mobilise.bookhub.enums.BookStatus;
import com.mobilise.bookhub.enums.TransactionType;
import com.mobilise.bookhub.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookUtils {
    private final BookRepository bookRepository;
    public void updateBookAvailability(Book book, int count, TransactionType action) {
        switch (action) {
            case BORROW:
                if (book.isAvailable()) {
                    long borrowedCopies = book.getBorrowedCopies() + count;
                    if (borrowedCopies == book.getTotalCopies()) {
                        book.setStatus(BookStatus.BORROWED);
                    } else {
                        book.setStatus(BookStatus.AVAILABLE);
                        book.setBorrowedCopies(borrowedCopies);
                        book.setTotalCopies(book.getTotalCopies() - count);
                    }
                }
                break;
            case RETURN:
                long returnedCopies = book.getBorrowedCopies() - count;
                book.setTotalCopies(book.getTotalCopies() + count);
                book.setBorrowedCopies(returnedCopies);
                book.setStatus(BookStatus.AVAILABLE);
                break;
            case PURCHASE:
                long purchasedCopies = book.getTotalCopies() - count;
                if (purchasedCopies == 0) {
                    book.setStatus(BookStatus.SOLD_OUT);
                } else {
                    if (book.isAvailable()) {
                        book.setStatus(BookStatus.AVAILABLE);
                    }
                }
                book.setTotalCopies(book.getTotalCopies() - count);
                break;
            default:
                throw new IllegalArgumentException("Invalid Transaction Type");
        }
        bookRepository.save(book);
    }
}
