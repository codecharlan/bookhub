package com.mobilise.bookhub.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
/**
 * Represents a response from all endpoint.
 *
 * @param <T> The type of the data contained in the response.
 * @author codecharlan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(String message, T data, int status) {
    /**
     * A static nested class representing a wrapper for the response data.
     *
     * @param <T> The type of the data contained in the wrapper.
     */
    @Getter
    @Setter
    public static class Wrapper<T> {
        private T data;
        private int pageNumber;
        private int pageSize;
        private int totalPages;
        private long totalElements;
        /**
         * Constructs a new instance of the Wrapper class.
         *
         * @param data The data to be wrapped.
         * @param pageNumber The current page number.
         * @param pageSize The number of elements per page.
         * @param totalPages The total number of pages.
         * @param totalElements The total number of elements.
         */
        public Wrapper(T data, int pageNumber, int pageSize, int totalPages, long totalElements) {
            this.data = data;
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
        }
    }

}
