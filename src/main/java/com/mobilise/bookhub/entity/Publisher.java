package com.mobilise.bookhub.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Publisher name must not be empty")
    @Column(unique = true)
    private String name;
    @Size(max = 100)
    private String location;
    @Size(max = 100)
    private String contactInformation;

    public Publisher(String name, String location, String contactInformation) {
        this.name = name;
        this.location = location;
        this.contactInformation = contactInformation;
    }
}
