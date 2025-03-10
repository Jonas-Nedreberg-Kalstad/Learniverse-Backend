package no.ntnu.idata2306.mapper;

import no.ntnu.idata2306.dto.course.CourseResponseDto;
import no.ntnu.idata2306.dto.course.CreateCourseDto;
import no.ntnu.idata2306.dto.course.UpdateCourseDto;
import no.ntnu.idata2306.model.Course;
import no.ntnu.idata2306.model.Review;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between Course entities and various Course DTOs.
 * Utilizes MapStruct for automatic generation of mapping implementations.
 */
@Mapper
public interface CourseMapper {

    /**
     * Instance of the CourseMapper to be used for mapping operations.
     */
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    /**
     * Converts a CreateCourseDto to a Course entity.
     *
     * @param createCourseDto the DTO containing course creation information
     * @return the Course entity
     */
    Course createCourseDtoToCourse(CreateCourseDto createCourseDto);

    /**
     * Converts an UpdateCourseDto to a Course entity.
     *
     * @param updateCourseDto the DTO containing course update information
     * @param course the Course entity to be updated
     */
    void updateCourseFromDto(UpdateCourseDto updateCourseDto, @MappingTarget Course course);

    /**
     * Converts a Course entity to a CourseResponseDto.
     *
     * @param course the Course entity
     * @return the CourseResponseDto
     */
    CourseResponseDto courseToResponseCourseDto(Course course);

    /**
     * Calculates the average rating of the reviews for a given course and sets it in the CourseResponseDto.
     * Also sets the number of ratings in the CourseResponseDto.
     *
     * @param courseResponseDto the CourseResponseDto to be updated
     * @param course the Course entity whose reviews' average rating is to be calculated
     */
    @AfterMapping
    default void calculateAndSetAverageRating(@MappingTarget CourseResponseDto courseResponseDto, Course course) {
        double sum = course.getReviews().stream()
                .mapToDouble(Review::getRating)
                .sum();
        double averageRating = sum / course.getReviews().size();
        courseResponseDto.setAverageRating(averageRating);
        courseResponseDto.setNumberOfReviews(course.getReviews().size());
    }
}
