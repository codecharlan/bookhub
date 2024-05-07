package com.mobilise.bookhub.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
/**
 * The Author entity represents an author of a book.
 * It contains the author's name, biography, and nationality.
 *
 * @author codecharlan
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Author {
    /**
     * The id of the author.
     * It is generated automatically and cannot be set manually.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the author.
     * It is required and must be between 2 and 100 characters long.
     *
     */
    @NotBlank(message = "Author name is required")
    @Size(min = 2, max = 100)
    private String name;

    /**
     * The biography of the author.
     * It can be up to 1000 characters long.
     *
     */
    @Size(max = 1000)
    private String biography;

    /**
     * The email address of the author.
     * It is required, must be a valid email address, and must be unique.
     *
     */
    @NotBlank(message = "Nationality is required")
    @Email(message = "Must be a valid email address")
    @Column(unique=true)
    private String emailAddress;

    /**
     * The nationality of the author.
     * It is required and must be between 2 and 100 characters long.
     *
     */
    @Size(min = 2, max = 100)
    private String nationality;

    /**
     * Constructor for creating a new Author object.
     *
     * @param name the author's name
     * @param biography the author's biography
     * @param nationality the author's nationality
     * @param emailAddress the author's email address
     */
    public Author(String name, String biography, String nationality, String emailAddress) {
        this.name = name;
        this.biography = biography;
        this.nationality = nationality;
        this.emailAddress = emailAddress;
    }
}
