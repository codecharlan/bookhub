package com.mobilise.bookhub.entity;

import com.mobilise.bookhub.enums.Gender;
import com.mobilise.bookhub.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity representing a registered user in the system.
 *
 * @author codecharlan
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "appuser")
@Entity
public class User implements Serializable {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the user.
     *
     */
    @Column(length = 100, nullable = false)
    @NotNull(message = "Fullname is required")
    private String fullName;

    /**
     * Email address of the user.
     *
     */
    @Column(length = 100, nullable = false)
    @Email(message = "Must match a proper email")
    @NotNull(message = "Email is required")
    private String email;

    /**
     * Password of the user.
     *
     */
    @Column(nullable = false)
    private String password;

    /**
     * Role of the user in the system.
     *
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    /**
     * Gender of the user.
     *
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    /**
     * Last login timestamp of the user.
     */
    @UpdateTimestamp
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss")
    private LocalDateTime lastLogin;

    /**
     * Balance of the user.
     */
    private BigDecimal balance;

    /**
     * List of books owned by the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Book> book = new ArrayList<>();

}