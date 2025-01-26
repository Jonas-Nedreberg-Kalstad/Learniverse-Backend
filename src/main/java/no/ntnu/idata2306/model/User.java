package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Schema(description = "User of the application", name = "User")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "ID of the user")
    private int id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "first_name", nullable = false, unique = false)
    @Schema(description = "First name of the user")
    private String firstName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "last_name", nullable = false, unique = false)
    @Schema(description = "Last name of the user")
    private String lastName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "email", nullable = false, unique = true, updatable = false)
    @Schema(description = "Email of the user")
    private String email;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "password", nullable = false, unique = false)
    @Schema(description = "Password of the user")
    private String password;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "phone_number", nullable = true, unique = false)
    @Schema(description = "phone number of the user")
    private String phoneNumber;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "created", nullable = false, unique = false)
    @Schema(description = "the date user was created")
    private LocalDateTime created;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "updated", nullable = true, unique = false)
    @Schema(description = "the date user was updated")
    private LocalDateTime updated;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "deleted", nullable = false, unique = false, updatable = true)
    @Schema(description = "If user account is deleted or not")
    private boolean deleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @Schema(description = "Roles the user has")
    private Set<Role> roles = new LinkedHashSet<>();

    /**
     * Constructor with parameters
     *
     * @param firstName    first name of user.
     * @param lastName     last name of user.
     * @param email        users email.
     * @param password     users password.
     * @param phoneNumber  users phone number.
     * @param created      date user were created
     */
    public User(String firstName, String lastName, String email, String password, String phoneNumber, LocalDateTime created) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.created = created;
        this.deleted = false;
    }

    public User(){

    }

}
