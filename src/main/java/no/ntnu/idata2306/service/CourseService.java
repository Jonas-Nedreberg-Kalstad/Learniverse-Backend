package no.ntnu.idata2306.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.course.CourseResponseDto;
import no.ntnu.idata2306.dto.course.CreateCourseDto;
import no.ntnu.idata2306.dto.course.UpdateCourseDto;
import no.ntnu.idata2306.mapper.course.CourseMapper;
import no.ntnu.idata2306.model.course.Course;
import no.ntnu.idata2306.model.Review;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.repository.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * Constructs a new instance of CourseService.
     *
     * @param courseRepository the repository for managing course data
     */
    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves all courses from the repository and converts them to CourseResponseDto objects.
     *
     * @return a list of CourseResponseDto objects representing all courses.
     */
    public List<CourseResponseDto> getAllCourses() {
        return this.courseRepository.findAll().stream()
                .map(CourseMapper.INSTANCE::courseToResponseCourseDto)
                .toList();
    }

    /**
     * Retrieves all active courses from the repository and converts them to CourseResponseDto objects.
     *
     * @return a list of CourseResponseDto objects representing all active courses.
     */
    public List<CourseResponseDto> getAllActiveCourses() {
        return this.courseRepository.findByActiveTrue().stream()
                .map(CourseMapper.INSTANCE::courseToResponseCourseDto)
                .toList();
    }

    /**
     * Retrieves all active courses from the repository based on pagination information.
     *
     * @param pageable the pagination information.
     * @return a page of active Course entities.
     */
    private Page<Course> getActiveCourses(Pageable pageable) {
        return this.courseRepository.findByActiveTrue(pageable);
    }

    /**
     * Finds and returns the most popular active courses based on the average rating of their reviews within the pagination constraints.
     *
     * @param pageable the pagination information.
     * @return a list of CourseResponseDto objects representing the most popular courses.
     * @throws EntityNotFoundException if no active courses are found.
     */
    public List<CourseResponseDto> getMostPopularCourses(Pageable pageable) {
        Page<Course> activeCourses = this.getActiveCourses(pageable);

        List<Course> mostPopularCourses = activeCourses.stream()
                .sorted(Comparator.comparingDouble(this::calculateAverageRating).reversed())
                .toList();

        if (mostPopularCourses.isEmpty()) {
            throw new EntityNotFoundException("No active courses found");
        }

        return mostPopularCourses.stream()
                .map(CourseMapper.INSTANCE::courseToResponseCourseDto)
                .toList();
    }

    /**
     * Calculates the average rating of a given course based on its reviews.
     *
     * @param course the Course entity for which to calculate the average rating.
     * @return the average rating of the course, or 0 if there are no reviews.
     */
    private double calculateAverageRating(Course course) {
        return course.getReviews().stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);
    }

    /**
     * Creates a new course with the provided information.
     *
     * @param createCourseDto the DTO containing the course information
     * @param user the user that created a course
     * @return the newly created CourseResponseDto object
     */
    public CourseResponseDto createCourse(CreateCourseDto createCourseDto, User user) {
        Course course = CourseMapper.INSTANCE.createCourseDtoToCourse(createCourseDto);
        course.setCreated(LocalDateTime.now());
        course.setCreatedBy(user);
        Course newCourse = this.courseRepository.save(course);
        return CourseMapper.INSTANCE.courseToResponseCourseDto(newCourse);
    }

    /**
     * Updates an existing course with the provided information.
     *
     * @param id the ID of the course to be updated
     * @param updateCourseDto the DTO containing the updated course information
     * @param user the user that updated a course
     * @return the updated CourseResponseDto object
     */
    public CourseResponseDto updateCourse(int id, UpdateCourseDto updateCourseDto, User user) {
        Course course = getCourseById(id);
        CourseMapper.INSTANCE.updateCourseFromDto(updateCourseDto, course);
        course.setUpdated(LocalDateTime.now());
        course.setUpdatedBy(user);
        Course updatedCourse = this.courseRepository.save(course);
        return CourseMapper.INSTANCE.courseToResponseCourseDto(updatedCourse);
    }

    /**
     * Marks a course as inactive.
     *
     * @param id the ID of the course to be marked as inactive
     * @return CourseResponseDto containing the updated course information
     */
    public CourseResponseDto deactivateCourse(int id) {
        Course course = getCourseById(id);
        course.setActive(false);
        this.courseRepository.save(course);
        return CourseMapper.INSTANCE.courseToResponseCourseDto(course);
    }

    /**
     * Calculates the average rating of the reviews for a given course.
     *
     * @param course the Course object whose reviews' average rating is to be calculated
     * @return the average rating of the reviews
     */
    public double calculatesRatingAverage(Course course){
        double sum = course.getReviews().stream()
                .mapToDouble(Review::getRating)
                .sum();
        return sum / course.getReviews().size();
    }

    /**
     * Finds a course by its ID.
     *
     * @param id the ID of the course to be found
     * @return the Course object if found
     * @throws EntityNotFoundException if the course with the specified ID is not found
     */
    public Course getCourseById(int id) {
        return this.courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + id));
    }
}
