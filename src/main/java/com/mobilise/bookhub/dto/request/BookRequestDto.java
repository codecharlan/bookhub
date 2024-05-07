package com.mobilise.bookhub.dto.request;

import com.mobilise.bookhub.entity.Author;
import com.mobilise.bookhub.entity.Publisher;
import com.mobilise.bookhub.enums.BookStatus;
import com.mobilise.bookhub.enums.Genre;
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
        String isbn,
        String title,
        String edition,
        String description,
        Genre genre,
        BookStatus status,
        Long totalCopies,
        Long borrowedCopies,
        Integer publicationYear,
        BigDecimal unitPriceOfBook,
        Author author,
        Set<Author> coAuthors,
        Publisher publisher
) implements Serializable {
}
