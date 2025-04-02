package no.ntnu.idata2306.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.review.ReviewRequestDto;
import no.ntnu.idata2306.dto.review.ReviewResponseDto;
import no.ntnu.idata2306.mapper.ReviewMapper;
import no.ntnu.idata2306.model.Review;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.model.course.Course;
import no.ntnu.idata2306.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseService courseService;

    /**
     * Constructs a new instance of ReviewService.
     *
     * @param reviewRepository the repository for managing review data
     * @param courseService the repository for managing course data
     */
    @Autowired
    public ReviewService(ReviewRepository reviewRepository, CourseService courseService) {
        this.reviewRepository = reviewRepository;
        this.courseService = courseService;
    }

    /**
     * Retrieves all reviews from the repository and converts them to ReviewResponseDto objects.
     *
     * @return a list of ReviewResponseDto objects representing all reviews.
     */
    public List<ReviewResponseDto> getAllReviews() {
        return this.reviewRepository.findAll().stream()
                .map(ReviewMapper.INSTANCE::reviewToReviewResponseDto)
                .toList();
    }

    /**
     * Retrieves the most helpful reviews for a given course by its ID with pagination.
     *
     * @param courseId the ID of the course to retrieve reviews for
     * @param page the page number for pagination
     * @param size the number of reviews per page
     * @return a list of ReviewResponseDto objects representing the most helpful reviews for the given course.
     */
    public List<ReviewResponseDto> getMostHelpfulReviewsByCourseId(int courseId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "helpfulVotes"));
        return this.reviewRepository.findByCourseId(courseId, pageable).stream()
                .map(ReviewMapper.INSTANCE::reviewToReviewResponseDto)
                .toList();
    }

    /**
     * Creates a new review with the provided information.
     *
     * @param reviewRequestDto the DTO containing the review information
     * @return the newly created ReviewResponseDto object
     */
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, User user) {
        Course course = courseService.findCourseById(reviewRequestDto.getCourseId());

        Review review = ReviewMapper.INSTANCE.reviewRequestDtoToReview(reviewRequestDto, user);
        review.setCourse(course);
        review.setCreated(LocalDateTime.now());

        this.reviewRepository.save(review);
        return ReviewMapper.INSTANCE.reviewToReviewResponseDto(review);
    }

    /**
     * Marks a review as deleted by setting the deleted field to true.
     *
     * @param id the ID of the review to be marked as deleted
     * @return ReviewResponseDto containing the updated review information
     */
    public ReviewResponseDto deleteReview(int id) {
        Review review = findReviewById(id);
        this.reviewRepository.delete(review);
        log.info("Review marked as deleted with ID: {}", id);
        return ReviewMapper.INSTANCE.reviewToReviewResponseDto(review);
    }

    /**
     * Retrieves a review by its ID and converts it to a ReviewResponseDto.
     *
     * @param id the ID of the review to retrieve
     * @return the ReviewResponseDto object if found
     * @throws EntityNotFoundException if the review with the specified ID is not found
     */
    public ReviewResponseDto getReviewById(int id) {
        Review review = findReviewById(id);
        return ReviewMapper.INSTANCE.reviewToReviewResponseDto(review);
    }

    /**
     * Finds a review by its ID.
     *
     * @param id the ID of the review to be found
     * @return the Review object if found
     * @throws EntityNotFoundException if the review with the specified ID is not found
     */
    private Review findReviewById(int id) {
        return this.reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Review not found with ID: {}", id);
                    return new EntityNotFoundException("Review not found with ID: " + id);
                });
    }
}