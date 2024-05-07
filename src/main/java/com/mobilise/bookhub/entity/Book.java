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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "ISBN is mandatory")
    private String isbn;

    @NotBlank(message = "Book title is mandatory")
    private String title;
    private String edition;
    @NotBlank(message = "Book description is mandatory")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Min(value = 0, message = "Total copies must be at least 0")
    private Long totalCopies;

    private Long borrowedCopies;

    @Column(nullable = false)
    @Min(value = 0, message = "Unit price cannot be negative")
    @NotNull(message = "Unit price is mandatory")
    private BigDecimal unitPriceOfBook;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull(message = "Book author is mandatory")
    private Author author;

    @ManyToMany
    @Nullable
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> coAuthors = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Min(value = 1000, message = "Publication year must be after 999")
    @PastYear(message = "Enter a correct publication year as your entry is in the future")  //Publication year cannot be in the future
    private Integer publicationYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public boolean isAvailable() {
        if (status == BookStatus.AVAILABLE &&  totalCopies > 0) {
            return true;
        } else {
            throw new ResourceNotFoundException("Book is out of Stock");
        }
    }
}
