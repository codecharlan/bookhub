package com.mobilise.bookhub.constants;

import org.springframework.http.HttpStatus;
/**
 * * mobilise-book-hub
 *  * Constants.java
 *  * May 05, 2024,
 * Constants class containing various API endpoints and related constants.
 *
 * @author charlancodes | <a href="https://www.linkedin.com/in/charlancodes/">...</a>
 * @version 1.0.0
 */
public class Constants {
    public static final int NETWORK_AUTHENTICATION_REQUIRED = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value();
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ID_URL = "/{id}";
    public static final String BASE_BOOK_API_URL = "/api/books";
    public static final String CREATE_URL = "/create";
    public static final String EDIT_URL = "/edit" + ID_URL;
    public static final String GET_ALL_URL = "/all";
    public static final String DELETE_URL = "/delete" + ID_URL;
    public static final String BORROW_BOOK_URL = "/borrow/{bookId}";
    public static final String RETURN_BOOK_URL = "/return/{bookId}";
    public static final String PURCHASE_BOOK_URL = "/purchase/{bookId}";
    public static final String BASE_USER_API_URL = "/api/users";
    public static final String REGISTER_URL = "/register";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String SEARCH_URL = "/search";
    public static final String BASE_REVIEW_API_URL = "/api/reviews";
    public static final String INVALID_ENUM_ENTRY =  "Invalid entry: make your entry again";

}
