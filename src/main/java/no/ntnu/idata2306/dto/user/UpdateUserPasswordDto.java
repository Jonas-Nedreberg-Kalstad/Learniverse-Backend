package no.ntnu.idata2306.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for updating user password.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserPasswordDto {
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
    private String password;
}
