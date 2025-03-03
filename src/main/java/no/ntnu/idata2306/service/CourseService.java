package no.ntnu.idata2306.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.course.CourseResponseDto;
import no.ntnu.idata2306.dto.course.CreateCourseDto;
import no.ntnu.idata2306.dto.course.UpdateCourseDto;
import no.ntnu.idata2306.mapper.CourseMapper;
import no.ntnu.idata2306.model.Course;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
