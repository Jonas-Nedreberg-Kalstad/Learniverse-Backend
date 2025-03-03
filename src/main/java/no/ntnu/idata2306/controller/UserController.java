package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.user.UserResponseDto;
import no.ntnu.idata2306.dto.user.UserSignUpDto;
import no.ntnu.idata2306.dto.user.UserUpdateDto;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@CrossOrigin()
@RequestMapping("/api")
@Tag(name = "User API", description = "Endpoints for user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     * Error code 500 is handled by global exception handler.
     *
     * @return a list of UserResponseDto objects representing all users.
     * @apiNote The password hash is not returned in the response for security reasons.
     */
    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/users")
    public List<UserResponseDto> getAll() {
        return userService.getAll();
    }

    /**
     * Retrieves all users that is not deleted.
     * Error code 500 is handled by global exception handler.
     *
     * @return a list of UserResponseDto objects representing all users.
     * @apiNote The password hash is not returned in the response for security reasons.
     */
    @Operation(summary = "Get all active users", description = "Retrieves a list of all users not deleted.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/activeUsers")
    public List<UserResponseDto> getAllActiveUsers() {
        return userService.getAllActiveUsers();
    }

    /**
     * Retrieves a user by their ID.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the user to retrieve
     * @return ResponseEntity with the UserResponseDto object and HTTP status
     * @apiNote The password hash is not returned in the response for security reasons.
     */
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/users/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable int id) {
        User user = this.userService.getUserById(id);
        UserResponseDto foundUser = new UserResponseDto(user);
        log.info("User found with ID: {}", id);
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }

    /**
     * Creates a new user.
     * Error code 400 and 500 is handled by global exception handler.
     *
     * @param userSignUpDto the DTO containing user sign-up information
     * @return ResponseEntity with the created UserResponseDto object and HTTP status
     * @apiNote The password hash is not returned in the response for security reasons.
     */
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided sign-up information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/anonymous/register")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserSignUpDto userSignUpDto) {
        UserResponseDto createdUser = this.userService.createUser(userSignUpDto);
        log.info("User created with ID: {}", createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    /**
     * Updates an existing user with the provided information.
     * Error code 400, 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the user to update
     * @param userUpdateDto the DTO containing updated user information
     * @return ResponseEntity with the updated UserResponseDto object and HTTP status
     * @apiNote The password hash is not returned in the response for security reasons.
     */
    @Operation(summary = "Update an existing user", description = "Updates an existing user with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/admin/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable int id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        UserResponseDto updatedUser = this.userService.updateUser(id, userUpdateDto);
        log.info("User updated with ID: {}", id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Marks a user as deleted by setting the deleted field to true.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the user to be marked as deleted
     * @return ResponseEntity with the UserResponseDto object and HTTP status
     * @apiNote The password hash is not returned in the response for security reasons.
     */
    @Operation(summary = "Delete a user", description = "Marks a user as deleted by setting the deleted field to true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User marked as deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable int id) {
        UserResponseDto user = this.userService.softDeleteUser(id);
        log.info("User updated with ID: {}", id);
        return new ResponseEntity<>(user, HttpStatus.NO_CONTENT);
    }

}
