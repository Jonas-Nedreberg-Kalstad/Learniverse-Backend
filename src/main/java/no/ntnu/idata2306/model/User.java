package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import no.ntnu.idata2306.model.course.Course;
import no.ntnu.idata2306.model.payment.PaymentCard;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = User.class)
@Schema(description = "User of the application", name = "User")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "ID of the user")
    private int id;

    @Column(name = "first_name", nullable = false, unique = false)
    @Schema(description = "First name of the user")
    private String firstName;

    @Column(name = "last_name", nullable = false, unique = false)
    @Schema(description = "Last name of the user")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Schema(description = "Email of the user")
    private String email;

    @Column(name = "password", nullable = false, unique = false)
    @Schema(description = "Password of the user")
    private String password;

    @Column(name = "phone_number", nullable = true, unique = false)
    @Schema(description = "Phone number of the user")
    private String phoneNumber;

    @Column(name = "created", nullable = false, unique = false)
    @Schema(description = "The date user was created")
    private LocalDateTime created;

    @Column(name = "updated", nullable = true, unique = false)
    @Schema(description = "The date user was updated")
    private LocalDateTime updated;

    @Column(name = "deleted", nullable = false, unique = false, updatable = true)
    @Schema(description = "If user account is deleted or not")
    private boolean deleted;

    @OneToMany(mappedBy = "createdBy")
    @Schema(description = "courses with the given user")
    private Set<Course> createdCourses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "updatedBy")
    @Schema(description = "courses with the given user")
    private Set<Course> updatedCourses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @Schema(description = "payment cards with the given user")
    private Set<PaymentCard> paymentCards = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @Schema(description = "reviews the user has given")
    private Set<Review> reviews = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "provider_id")
    @Schema(description = "provider associated with the given user")
    private Provider provider;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @Schema(description = "Roles the user has")
    private Set<Role> roles = new LinkedHashSet<>();

}