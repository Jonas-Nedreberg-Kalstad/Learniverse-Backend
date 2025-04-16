package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.course.CourseEnrollmentsResponseDto;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.service.CourseEnrollmentsService;
import no.ntnu.idata2306.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing course enrollments.
 */
@Slf4j
@Validated
@RestController
@CrossOrigin()
@RequestMapping("/api")
@Tag(name = "Course Enrollments API", description = "Endpoints for course enrollments")
public class CourseEnrollmentsController {

    private final CourseEnrollmentsService courseEnrollmentsService;
    private final UserService userService;

    @Autowired
    public CourseEnrollmentsController(CourseEnrollmentsService courseEnrollmentsService, UserService userService) {
        this.courseEnrollmentsService = courseEnrollmentsService;
        this.userService = userService;
    }

    /**
     * Retrieves all course enrollments.
     * Error code 500 is handled by global exception handler.
     *
     * @return a list of CourseEnrollmentsResponseDto objects representing all course enrollments.
     */
    @Operation(summary = "Get all course enrollments", description = "Retrieves a list of all course enrollments.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course enrollments retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseEnrollmentsResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/course-enrollments")
    public List<CourseEnrollmentsResponseDto> getAllCourseEnrollments() {
        return courseEnrollmentsService.getAllCourseEnrollments();
    }

    /**
     * Retrieves a course enrollment by its ID.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the course enrollment to retrieve
     * @return ResponseEntity with the CourseEnrollmentsResponseDto object and HTTP status
     */
    @Operation(summary = "Get course enrollment by ID", description = "Retrieves a course enrollment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course enrollment retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseEnrollmentsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Course enrollment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/course-enrollments/{id}")
    public ResponseEntity<CourseEnrollmentsResponseDto> getCourseEnrollmentById(@PathVariable int id) {
        CourseEnrollmentsResponseDto courseEnrollment = courseEnrollmentsService.getCourseEnrollmentById(id);
        log.info("Course enrollment found with ID: {}", id);
        return new ResponseEntity<>(courseEnrollment, HttpStatus.OK);
    }

    /**
     * Retrieves course enrollment history for a given user.
     * Error code 500 is handled by global exception handler.
     *
     * @return a list of CourseEnrollmentsResponseDto objects representing the user's course enrollment history.
     */
    @Operation(summary = "Get all course enrollments for a user", description = "Retrieves all course enrollments for a given user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course enrollments retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseEnrollmentsResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/course-enrollments/history")
    public List<CourseEnrollmentsResponseDto> getCourseEnrollmentsHistory() {
        User user = userService.getSessionUser();
        return courseEnrollmentsService.getCourseEnrollmentsHistory(user);
    }
}
