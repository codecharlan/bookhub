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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Author name is required")
    @Size(min = 2, max = 100)
    private String name;
    @Size(max = 1000)
    private String biography;
    @NotBlank(message = "Nationality is required")
    @Email(message = "Must be a valid email address")
    @Column(unique=true)
    private String emailAddress;
    @Size(min = 2, max = 100)
    private String nationality;

    public Author(String name, String biography, String nationality, String emailAddress) {
        this.name = name;
        this.biography = biography;
        this.nationality = nationality;
        this.emailAddress = emailAddress;
    }
}
