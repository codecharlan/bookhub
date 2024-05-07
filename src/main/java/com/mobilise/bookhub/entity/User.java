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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "appuser")
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    @NotNull(message = "Fullname is required")
    private String fullName;
    @Column(length = 100, nullable = false)
    @Email(message = "Must match a proper email")
    @NotNull(message = "Email is required")
    private String email;
    @Column(length = 100, nullable = false)
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])([a-zA-Z0-9@$!%*?&]{8,})$",
//            message = "Password must be at least 8 characters long, " +
//                    "contain at least one uppercase letter, one lowercase letter")
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;
    @UpdateTimestamp
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss")
    private LocalDateTime lastLogin;
    private BigDecimal balance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Book> book = new ArrayList<>();

    public void setRole(Role role) {
        switch (role) {
            case USER:
            case ADMINISTRATOR:
                this.role = role;
                break;
            default:
                throw new IllegalArgumentException("Role must be either USER or ADMINISTRATOR");
        }
    }

    public void setGender(Gender gender) {
        switch (gender) {
            case FEMALE:
            case MALE:
                this.gender = gender;
                break;
            default:
                throw new IllegalArgumentException("Gender must be male or female");
        }
    }

}
