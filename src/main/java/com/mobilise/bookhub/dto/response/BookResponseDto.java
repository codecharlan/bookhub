package com.mobilise.bookhub.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mobilise.bookhub.entity.Author;
import com.mobilise.bookhub.entity.Publisher;
import com.mobilise.bookhub.enums.BookStatus;
import com.mobilise.bookhub.enums.Genre;
import lombok.Builder;

import java.math.BigDecimal;
/**
 * A DTO (Data Transfer Object) representing a book's information.
 *
 * @author codecharlan
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookResponseDto(
        Long id,
        String isbn,
        String title,
        String edition,
        String description,
        Integer publicationYear,
        Genre genre,
        BookStatus status,
        Long totalCopies,
        Long borrowedCopies,
        BigDecimal unitPriceOfBook,
        Author author,
        Publisher publisher
) {
}
