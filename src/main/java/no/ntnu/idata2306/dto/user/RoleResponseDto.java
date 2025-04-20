package no.ntnu.idata2306.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for Role Response.
 * This class is used to transfer role data between different layers of the application.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDto {
    private int id;
    private String role;
}
