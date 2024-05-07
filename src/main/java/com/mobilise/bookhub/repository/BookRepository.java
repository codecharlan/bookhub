package com.mobilise.bookhub.repository;

import com.mobilise.bookhub.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface for the Book repository, extending {@link JpaRepository}.
 * This interface provides custom query methods for interacting with the Book entity.
 *
 * @author codecharlan

/**
 * The BookRepository interface extends {@link JpaRepository} and provides custom query methods for interacting with the Book entity.
 *
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds a book by its title and author's name.
     *
     * @param title the title of the book to find
     * @param authorsName the name of the author of the book to find
     * @return an {@link Optional} containing the found book, or an empty {@link Optional} if no book is found
     */
    Optional<Book> findByTitleAndAuthor_Name(String title, String authorsName);

    /**
     * Finds all books whose title contains the specified search term, ignoring case.
     *
     * @param searchTerm the search term to use
     * @param pageable the {@link Pageable} object to define the pagination and sorting of the results
     * @return a {@link Page} containing the matching books, sorted and paginated according to the {@link Pageable} object
     */
    Page<Book> findByTitleContainingIgnoreCase(String searchTerm, Pageable pageable);

    /**
     * Finds all books whose title or author's name contains the specified search term, ignoring case.
     *
     * @param searchTerm the search term to use
     * @param searchTerm1 an additional search term to use, for matching author's name
     * @param pageable the {@link Pageable} object to define the pagination and sorting of the results
     * @return a {@link Page} containing the matching books, sorted and paginated according to the {@link Pageable} object
     */
    Page<Book> findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCase(String searchTerm, String searchTerm1, Pageable pageable);
}
