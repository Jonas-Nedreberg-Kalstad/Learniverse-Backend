package no.ntnu.idata2306.mapper;

import no.ntnu.idata2306.dto.course.CourseResponseDto;
import no.ntnu.idata2306.dto.course.CreateCourseDto;
import no.ntnu.idata2306.dto.course.UpdateCourseDto;
import no.ntnu.idata2306.model.Course;
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
     */
    void updateCourseFromDto(UpdateCourseDto updateCourseDto, @MappingTarget Course course);

    /**
     * Converts a Course entity to a CourseResponseDto.
     *
     * @param course the Course entity
     * @return the CourseResponseDto
     */
    CourseResponseDto courseToResponseCourseDto(Course course);
}
