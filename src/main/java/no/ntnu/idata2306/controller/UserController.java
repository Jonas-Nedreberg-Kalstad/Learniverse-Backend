package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.UserResponseDto;
import no.ntnu.idata2306.dto.UserSignUpDto;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
     * Creates a new user.
     *
     * @param userSignUpDto the DTO containing user sign-up information
     * @return  ResponseEntity with the created UserResponseDto object and HTTP status
     * @apiNote The password is not returned in the response for security reasons.
     */
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided sign-up information.")
    @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")
    @PostMapping("/anonymous")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserSignUpDto userSignUpDto) {
        try {
            User createdUser = userService.createUser(userSignUpDto);
            log.info("User created with ID: {}", createdUser.getId());
            UserResponseDto userResponse = new UserResponseDto(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (Exception e) {
            log.error("Error creating user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
