package com.mobilise.bookhub.dto.request;

import com.mobilise.bookhub.entity.Author;
import com.mobilise.bookhub.entity.Publisher;
import com.mobilise.bookhub.enums.BookStatus;
import com.mobilise.bookhub.enums.Genre;
import com.mobilise.bookhub.utils.validator.PastYear;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * A DTO (Data Transfer Object) representing a book request.
 * This class is used to transfer data between the client and the server.
 *
 * @author charlancodes
 */
@Builder
public record BookRequestDto(
        Long id,
        @NotBlank(message = "ISBN is mandatory")
        String isbn,
        @NotBlank(message = "Book title is mandatory")
        String title,
        String edition,
        @NotBlank(message = "Book description is mandatory")
        String description,
        Genre genre,
        BookStatus status,
        @Min(value = 0, message = "Total copies must be at least 0")
        Long totalCopies,
        Long borrowedCopies,

        @Min(value = 1000, message = "Publication year must be after 999")
        @PastYear(message = "Enter a correct publication year as your entry is in the future")  //Publication year cannot be in the future
        Integer publicationYear,
        @Min(value = 0, message = "Unit price cannot be negative")
        @NotNull(message = "Unit price is mandatory")
        BigDecimal unitPriceOfBook,
        @NotNull(message = "Book author is mandatory")
        Author author,
        Set<Author> coAuthors,
        Publisher publisher
) implements Serializable {
}
