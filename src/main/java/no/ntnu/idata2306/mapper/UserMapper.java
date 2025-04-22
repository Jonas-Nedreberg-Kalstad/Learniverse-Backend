package no.ntnu.idata2306.mapper;

import no.ntnu.idata2306.dto.user.*;
import no.ntnu.idata2306.model.Role;
import no.ntnu.idata2306.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

/**
 * Mapper interface for converting between User entities and various User DTOs.
 * Utilizes MapStruct for automatic generation of mapping implementations.
 */
@Mapper
public interface UserMapper {

    /**
     * Instance of the UserMapper to be used for mapping operations.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Converts a UserSignUpDto to a User entity, ignoring the password field.
     *
     * @param signUpDto the DTO containing user sign-up information
     * @return the User entity
     */
    @Mapping(target = "password", ignore = true)
    User userSignupDtoToUser(UserSignUpDto signUpDto);

    /**
     * Converts a UserSignUpDto to a User entity and encrypts the password.
     *
     * @param signUpDto the DTO containing user sign-up information
     * @param passwordEncoder the PasswordEncoder to encrypt the password
     * @return the User entity with the encrypted password
     */
    default User userSignupDtoToUserWithPassword(UserSignUpDto signUpDto, @Lazy PasswordEncoder passwordEncoder) {
        User user = userSignupDtoToUser(signUpDto);
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setRoles(new HashSet<>()); // initializing hashset due to roles is always null for create
        return user;
    }

    /**
     * Converts a User entity to a UserResponseDto.
     *
     * @param user the User entity
     * @return the UserResponseDto
     */
    UserResponseDto userToUserResponseDto(User user);

    /**
     * Updates an existing User entity with the provided UserUpdateDto, ignoring the password field.
     *
     * @param userUpdateDto the DTO containing user update information
     * @param user the User entity to be updated
     */
    @Mapping(target = "password", ignore = true)
    void updateUserFromDto(UserUpdateDto userUpdateDto, @MappingTarget User user);

    /**
     * Updates the password of an existing User entity with the provided UpdateUserPasswordDto and encrypts the password.
     *
     * @param userPasswordDto the DTO containing the new password
     * @param user the User entity to be updated
     * @param passwordEncoder the PasswordEncoder to encrypt the new password
     */
    default void updateUserPassword(UpdateUserPasswordDto userPasswordDto, @MappingTarget User user, @Lazy PasswordEncoder passwordEncoder) {
        user.setPassword(passwordEncoder.encode(userPasswordDto.getPassword()));
    }

    /**
     * Converts a Role entity to a RoleResponseDto.
     *
     * @param role the Role entity
     * @return the RoleResponseDto
     */
    RoleResponseDto roleToRoleResponseDto(Role role);

}
