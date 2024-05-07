package com.mobilise.bookhub.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * The Publisher class represents a publisher entity in the BookHub application.
 * It contains the publisher's name, location, and contact information.
 *
 * @author codecharlan
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Publisher {
    /**
     * The unique identifier for the publisher.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the publisher.
     * Must not be empty.
     * Unique within the database.
     */
    @NotBlank(message = "Publisher name must not be empty")
    @Column(unique = true)
    private String name;

    /**
     * The location of the publisher.
     * Maximum length of 100 characters.
     */
    @Size(max = 100)
    private String location;

    /**
     * The contact information of the publisher.
     * Maximum length of 100 characters.
     */
    @Size(max = 100)
    private String contactInformation;

    /**
     * Constructor for creating a new Publisher object.
     *
     * @param name the name of the publisher
     * @param location the location of the publisher
     * @param contactInformation the contact information of the publisher
     */
    public Publisher(String name, String location, String contactInformation) {
        this.name = name;
        this.location = location;
        this.contactInformation = contactInformation;
    }
}
