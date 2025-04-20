package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.course.CourseResponseDto;
import no.ntnu.idata2306.dto.course.CreateCourseDto;
import no.ntnu.idata2306.dto.course.UpdateCourseDto;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.service.CourseService;
import no.ntnu.idata2306.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@CrossOrigin()
@RequestMapping("/api")
@Tag(name = "Course API", description = "Endpoints for course")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    /**
     * Retrieves all courses.
     * Error code 500 is handled by global exception handler.
     *
     * @return a list of CourseResponseDto objects representing all courses.
     */
    @Operation(summary = "Get all courses", description = "Retrieves a list of all courses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/courses")
    public List<CourseResponseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    /**
     * Retrieves all active courses.
     * Error code 500 is handled by global exception handler.
     *
     * @return a list of CourseResponseDto objects representing all active courses.
     */
    @Operation(summary = "Get all active courses", description = "Retrieves a list of all active courses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/anonymous/activeCourses")
    public List<CourseResponseDto> getAllActiveCourses() {
        return courseService.getAllActiveCourses();
    }

    /**
     * Retrieves all courses for a specific provider.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @return a list of CourseResponseDto objects representing all courses for the provider.
     */
    @Operation(summary = "Get all provider courses", description = "Retrieves a list of all courses for the provider.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "No access. User has not been associated with a valid provider."),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/provider/courses")
    public ResponseEntity<List<CourseResponseDto>> getAllProviderCourses() {
        User user = this.userService.getSessionUser();
        if (user.getProvider() != null) {
            List<CourseResponseDto> courses = this.courseService.getAllCoursesByProviderId(user.getProvider().getId());
            log.info("Courses retrieved for provider with ID: {}", user.getProvider().getId());
            return new ResponseEntity<>(courses, HttpStatus.OK);
        }
        log.warn("User with ID: {} does not have access to get all courses for the provider", user.getId());
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     * Retrieves a list of the most popular courses.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param page the page number to retrieve.
     * @param size the size of the page to retrieve.
     * @return a list of CourseResponseDto objects representing the most popular courses.
     */
    @Operation(summary = "Get most popular courses", description = "Retrieves a list of the most popular courses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Course(s) not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/anonymous/mostPopularCourses")
    public List<CourseResponseDto> getMostPopularCourses(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.courseService.getMostPopularCourses(pageable);
    }

    /**
     * Retrieves a course by its ID.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the course to retrieve
     * @return ResponseEntity with the CourseResponseDto object and HTTP status
     */
    @Operation(summary = "Get course by ID", description = "Retrieves a course by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Access to the requested course is forbidden."),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/anonymous/courses/{id}")
    public ResponseEntity<CourseResponseDto> getCourse(@PathVariable int id) {
        CourseResponseDto course = this.courseService.getCourseById(id);

        if(course.isActive()){
            log.info("Course found with ID: {}", id);
            return new ResponseEntity<>(course, HttpStatus.OK);
        }

        log.error("Forbidden access attempt for course with ID: {}", id);
        return new ResponseEntity<>(course, HttpStatus.FORBIDDEN);
    }

    /**
     * Creates a new course.
     * Error code 400 and 500 is handled by global exception handler.
     *
     * @param createCourseDto the DTO containing course creation information
     * @return ResponseEntity with the created CourseResponseDto object and HTTP status
     */
    @Operation(summary = "Create a new course", description = "Creates a new course with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/admin/courses",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseResponseDto> createCourse(@Valid @RequestBody CreateCourseDto createCourseDto) {
        User user = this.userService.getSessionUser();
        CourseResponseDto createdCourse = this.courseService.createCourse(createCourseDto, user);
        log.info("Course created with ID: {}", createdCourse.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    /**
     * Creates a new course as a provider.
     * Error code 400, 404 and 500 is handled by global exception handler.
     *
     * @param createCourseDto the DTO containing course creation information
     * @return ResponseEntity with the created CourseResponseDto object and HTTP status
     */
    @Operation(summary = "Create a new course", description = "Creates a new course with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "403", description = "User does not have access to create a course"),
            @ApiResponse(responseCode = "404", description = "Provider not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/provider/courses",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseResponseDto> createCourseProvider(@Valid @RequestBody CreateCourseDto createCourseDto) {
        User user = this.userService.getSessionUser();
        if (user.getProvider() != null) {
            CourseResponseDto createdCourse = this.courseService.createCourseProvider(createCourseDto, user);
            log.info("Course created with ID: {}, and provider ID: {}", createdCourse.getId(), user.getProvider().getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        }

        log.warn("User with ID: {} does not have access to create a course", user.getId());
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     * Updates an existing course with the provided information.
     * Error code 400, 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the course to update
     * @param updateCourseDto the DTO containing updated course information
     * @return ResponseEntity with the updated CourseResponseDto object and HTTP status
     */
    @Operation(summary = "Update an existing course", description = "Updates an existing course with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/admin/courses/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(@PathVariable int id, @Valid @RequestBody UpdateCourseDto updateCourseDto) {
        User user = this.userService.getSessionUser();
        CourseResponseDto updatedCourse = this.courseService.updateCourse(id, updateCourseDto, user);
        log.info("Course updated with ID: {}", id);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    /**
     * Updates an existing course with the provided information.
     * Error code 400, 404 and 500 is handled by global exception handler.
     *
     * @param courseId the ID of the course to update
     * @param updateCourseDto the DTO containing updated course information
     * @return ResponseEntity with the updated CourseResponseDto object and HTTP status
     */
    @Operation(summary = "Update an existing course", description = "Updates an existing course with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "403", description = "User does not have access to update the course due to user not having a valid provider id, " +
                                                             "or the provider does not own the course to be updated"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/provider/courses/{courseId}")
    public ResponseEntity<CourseResponseDto> updateCourseProvider(@PathVariable int courseId,
                                                                  @Valid @RequestBody UpdateCourseDto updateCourseDto) {
        User user = this.userService.getSessionUser();
        if (user.getProvider() == null) {
            log.warn("User with ID: {} does not have access to update the course with ID: {}", user.getId(), courseId);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        CourseResponseDto updatedCourse = this.courseService.updateCourseProvider(courseId, updateCourseDto, user);

        if (updatedCourse == null){
            log.warn("User with ID: {} tried to update a course that does not belong to the provider with ID: {}", user.getId(), user.getProvider().getId());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        log.info("Course updated with ID: {}", courseId);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    /**
     * Marks a course as deleted by setting the deleted field to true.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the course to be marked as deleted
     * @return ResponseEntity with the CourseResponseDto object and HTTP status
     */
    @Operation(summary = "Delete a course", description = "Marks a course as deleted by setting the deleted field to true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course marked as deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/admin/courses/{id}")
    public ResponseEntity<CourseResponseDto> deleteCourse(@PathVariable int id) {
        CourseResponseDto course = this.courseService.deactivateCourse(id);
        log.info("Course updated with ID: {}", id);
        return new ResponseEntity<>(course, HttpStatus.NO_CONTENT);
    }

}
