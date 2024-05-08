package com.mobilise.bookhub.entity;

import com.mobilise.bookhub.enums.BookStatus;
import com.mobilise.bookhub.enums.Genre;
import com.mobilise.bookhub.exception.ResourceNotFoundException;
import com.mobilise.bookhub.utils.validator.PastYear;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * The Book entity represents a book in the BookHub application.
 * It contains information such as ISBN, title, edition, description, genre, status,
 * total copies, borrowed copies, unit price, author, co-authors, publisher, and publication year.
 *
 * @author codecharlan
 * @version 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Builder
@Entity
public class Book {
    /**
     * The unique identifier for the book.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique identifier for the book.
     * Required field.
     *
     */
    private String isbn;

    /**
     * The title of the book.
     *
     */
    private String title;

    /**
     * The edition of the book.
     *
     */
    private String edition;

    /**
     * The description of the book.
     *
     */
    private String description;

    /**
     * The genre of the book.
     *
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    /**
     * The status of the book.
     *
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatus status;

    /**
     * The total number of copies of the book.
     *
     */
    private Long totalCopies;

    /**
     * The number of borrowed copies of the book.
     *
     */
    private Long borrowedCopies;

    /**
     * The unit price of the book.
     *
     */
    @Column(nullable = false)
    private BigDecimal unitPriceOfBook;

    /**
     * The author of the book.
     *
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    /**
     * The co-authors of the book.
     *
     */
    @ManyToMany
    @Nullable
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> coAuthors = new HashSet<>();

    /**
     * The publisher of the book.
     *
     */
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    /**
     * The publication year of the book.
     *
     */

    private Integer publicationYear;

    /**
     * The user who owns the book.
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Checks if the book is available for borrowing.
     *
     * @return true if the book is available, false otherwise
     */
    public boolean isAvailable() {
        if (status == BookStatus.AVAILABLE &&  totalCopies > 0) {
            return true;
        } else {
            throw new ResourceNotFoundException("Book is out of Stock");
        }
    }
}
