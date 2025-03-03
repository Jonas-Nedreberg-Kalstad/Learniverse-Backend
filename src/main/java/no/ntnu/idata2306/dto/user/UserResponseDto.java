package no.ntnu.idata2306.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.model.User;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for user responses.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDateTime created;
    private LocalDateTime updated;
    private boolean deleted;

    /**
     * Constructor that takes a User object.
     *
     * @param user the User object containing user information
     */
    public UserResponseDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.created = user.getCreated();
        this.updated = user.getUpdated();
        this.deleted = user.isDeleted();
    }
}
