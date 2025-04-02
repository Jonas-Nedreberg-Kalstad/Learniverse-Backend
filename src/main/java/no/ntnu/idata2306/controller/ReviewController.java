package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.review.ReviewRequestDto;
import no.ntnu.idata2306.dto.review.ReviewResponseDto;
import no.ntnu.idata2306.mapper.ReviewMapper;
import no.ntnu.idata2306.model.Review;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.service.ReviewService;
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
@Tag(name = "Review API", description = "Endpoints for reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    /**
     * Retrieves all reviews.
     * Error code 500 is handled by global exception handler.
     *
     * @return a list of ReviewResponseDto objects representing all reviews.
     */
    @Operation(summary = "Get all reviews", description = "Retrieves a list of all reviews.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/reviews")
    public List<ReviewResponseDto> getAllReviews() {
        return reviewService.getAllReviews();
    }


    /**
     * Retrieves the most helpful reviews for a given course by its ID with pagination.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param courseId the ID of the course to retrieve reviews for
     * @param page the page number for pagination (default is 0)
     * @param size the number of reviews per page (default is 5)
     * @return a list of ReviewResponseDto objects representing the most helpful reviews for the given course.
     */
    @Operation(summary = "Get most helpful reviews for a course", description = "Retrieves the most helpful reviews for a given course by its ID with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/anonymous/reviews/course/{courseId}/helpful")
    public ResponseEntity<List<ReviewResponseDto>> getMostHelpfulReviewsByCourseId(@PathVariable int courseId,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "5") int size) {
        List<ReviewResponseDto> reviews = this.reviewService.getMostHelpfulReviewsByCourseId(courseId, page, size);
        log.info("Most helpful reviews found for course ID: {}", courseId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * Retrieves a review by its ID.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the review to retrieve
     * @return ResponseEntity with the ReviewResponseDto object and HTTP status
     */
    @Operation(summary = "Get review by ID", description = "Retrieves a review by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Review not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/reviews/{id}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable int id) {
        ReviewResponseDto review = this.reviewService.getReviewById(id);
        log.info("Review found with ID: {}", id);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    /**
     * Creates a new review.
     * Error code 400 and 500 is handled by global exception handler.
     *
     * @param reviewRequestDto the DTO containing review information
     * @return ResponseEntity with the created ReviewResponseDto object and HTTP status
     */
    @Operation(summary = "Create a new review", description = "Creates a new review with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/user/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        User user = this.userService.getSessionUser();
        ReviewResponseDto createdReview = this.reviewService.createReview(reviewRequestDto, user);
        log.info("Review created with ID: {}", createdReview.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    /**
     * Marks a review as deleted by setting the deleted field to true.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the review to be marked as deleted
     * @return ResponseEntity with the ReviewResponseDto object and HTTP status
     */
    @Operation(summary = "Delete a review", description = "Marks a review as deleted by setting the deleted field to true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review marked as deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Review not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/admin/reviews/{id}")
    public ResponseEntity<ReviewResponseDto> deleteReview(@PathVariable int id) {
        ReviewResponseDto review = this.reviewService.deleteReview(id);
        log.info("Review updated with ID: {}", id);
        return new ResponseEntity<>(review, HttpStatus.NO_CONTENT);
    }
}
