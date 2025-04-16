package no.ntnu.idata2306.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.course.CourseEnrollmentsResponseDto;
import no.ntnu.idata2306.mapper.course.CourseEnrollmentsMapper;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.model.course.CourseEnrollments;
import no.ntnu.idata2306.repository.course.CourseEnrollmentsRepository;
import no.ntnu.idata2306.util.repository.RepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing course enrollments.
 */
@Slf4j
@Service
public class CourseEnrollmentsService {

    private final CourseEnrollmentsRepository courseEnrollmentsRepository;

    @Autowired
    public CourseEnrollmentsService(CourseEnrollmentsRepository courseEnrollmentsRepository) {
        this.courseEnrollmentsRepository = courseEnrollmentsRepository;
    }

    /**
     * Retrieves all course enrollments from the repository and converts them to CourseEnrollmentsResponseDto objects.
     *
     * @return a list of CourseEnrollmentsResponseDto objects representing all course enrollments.
     */
    public List<CourseEnrollmentsResponseDto> getAllCourseEnrollments() {
        return this.courseEnrollmentsRepository.findAll().stream()
                .map(CourseEnrollmentsMapper.INSTANCE::courseEnrollmentsToCourseEnrollmentsResponseDto)
                .toList();
    }

    /**
     * Finds a course enrollment by its ID.
     *
     * @param id the ID of the course enrollment to be found
     * @return the CourseEnrollments object if found
     * @throws EntityNotFoundException if the course enrollment with the specified ID is not found
     */
    public CourseEnrollments findCourseEnrollmentById(int id) {
        return RepositoryUtils.findEntityById(courseEnrollmentsRepository::findById, id, CourseEnrollments.class);
    }

    /**
     * Retrieves a course enrollment by its ID and converts it to a CourseEnrollmentsResponseDto.
     *
     * @param id the ID of the course enrollment to retrieve
     * @return the CourseEnrollmentsResponseDto object if found
     * @throws EntityNotFoundException if the course enrollment with the specified ID is not found
     */
    public CourseEnrollmentsResponseDto getCourseEnrollmentById(int id) {
        CourseEnrollments courseEnrollment = findCourseEnrollmentById(id);
        return CourseEnrollmentsMapper.INSTANCE.courseEnrollmentsToCourseEnrollmentsResponseDto(courseEnrollment);
    }

    /**
     * Retrieves course enrollment history for a given user from the repository and converts them to CourseEnrollmentsResponseDto objects.
     *
     * @param user the user whose course enrollments are to be retrieved.
     * @return a list of CourseEnrollmentsResponseDto objects representing the course enrollment history.
     */
    public List<CourseEnrollmentsResponseDto> getCourseEnrollmentsHistory(User user) {
        return this.courseEnrollmentsRepository.findByUserId(user.getId()).stream()
                .map(CourseEnrollmentsMapper.INSTANCE::courseEnrollmentsToCourseEnrollmentsResponseDto)
                .toList();
    }
}
