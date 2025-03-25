package no.ntnu.idata2306.mapper.course;

import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.model.course.Course;
import no.ntnu.idata2306.model.course.CourseEnrollments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

/**
 * Mapper for converting between entities and CourseEnrollments entity.
 */
@Mapper
public interface CourseEnrollmentsMapper {

    CourseEnrollmentsMapper INSTANCE = Mappers.getMapper(CourseEnrollmentsMapper.class);

    /**
     * Converts entities to a CourseEnrollments entity.
     *
     * @param course the Course entity
     * @param user the User entity
     * @param created the creation date
     * @param enrolled the enrollment status
     * @return the CourseEnrollments entity
     */
    @Mapping(source = "course", target = "course")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "course.startDate", target = "startDate")
    @Mapping(source = "course.endDate", target = "endDate")
    @Mapping(source = "course.credit.credit", target = "credit")
    @Mapping(source = "course.hoursPerWeek.hours", target = "hoursPerWeek")
    @Mapping(source = "created", target = "created")
    @Mapping(source = "enrolled", target = "enrolled")
    @Mapping(target = "id", ignore = true) // Ignore the id property
    CourseEnrollments toCourseEnrollments(Course course, User user, LocalDateTime created, boolean enrolled);
}
