package no.ntnu.idata2306.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.dto.ProviderResponseDto;

import java.time.LocalDateTime;
import java.util.List;

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
    private ProviderResponseDto provider;
    private List<RoleResponseDto> roles;
}
