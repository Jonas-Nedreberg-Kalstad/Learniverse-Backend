package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.UserResponseDto;
import no.ntnu.idata2306.dto.UserSignUpDto;
import no.ntnu.idata2306.dto.UserUpdateDto;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin()
@RequestMapping("/api")
@Tag(name = "User API", description = "Endpoints for user")
public class UserController {

    private final UserService userService;

    private static final String INTERNAL_SERVER_ERROR = "Internal server error while retrieving user with ID: {}";
    private static final String USER_NOT_FOUND = "User not found with ID: {}";
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     *
     * @return a list of UserResponseDto objects representing all users.
     */
    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/admin/users")
    public List<UserResponseDto> getAll() {
        return userService.getAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return ResponseEntity with the UserResponseDto object and HTTP status
     */
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID.")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/admin/users/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable int id) {
        try {
            User user = this.userService.findUserById(id);
            UserResponseDto foundUser = new UserResponseDto(user);
            log.info("User found with ID: {}", id);
            return new ResponseEntity<>(foundUser, HttpStatus.OK);
        }catch (RuntimeException e){
            log.warn(USER_NOT_FOUND, id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new user.
     *
     * @param userSignUpDto the DTO containing user sign-up information
     * @return  ResponseEntity with the created UserResponseDto object and HTTP status
     * @apiNote The password hash is not returned in the response for security reasons.
     */
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided sign-up information.")
    @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")
    @PostMapping("/anonymous/register")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserSignUpDto userSignUpDto) {
        try {
            UserResponseDto createdUser = this.userService.createUser(userSignUpDto);
            log.info("User created with ID: {}", createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            log.error("Error creating user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    /**
     * Updates an existing user with the provided information.
     *
     * @param id the ID of the user to update
     * @param userUpdateDto the DTO containing updated user information
     * @return ResponseEntity with the updated UserResponseDto object and HTTP status
     */
    @Operation(summary = "Update an existing user", description = "Updates an existing user with the provided information.")
    @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PatchMapping("/admin/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable int id, @RequestBody UserUpdateDto userUpdateDto) {

        try{
            UserResponseDto updatedUser = this.userService.updateUser(id, userUpdateDto);
            log.info("User updated with ID: {}", id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e){
            log.warn(USER_NOT_FOUND, id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e){
            log.error(INTERNAL_SERVER_ERROR, id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Marks a user as deleted by setting the deleted field to true.
     *
     * @param id the ID of the user to be marked as deleted
     * @return ResponseEntity with the UserResponseDto object and HTTP status
     */
    @Operation(summary = "Delete a user", description = "Marks a user as deleted by setting the deleted field to true.")
    @ApiResponse(responseCode = "200", description = "User marked as deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable int id) {
        try{
            UserResponseDto user = this.userService.softDeleteUser(id);
            log.info("User updated with ID: {}", id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e){
            log.warn(USER_NOT_FOUND, id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e){
            log.error(INTERNAL_SERVER_ERROR, id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
