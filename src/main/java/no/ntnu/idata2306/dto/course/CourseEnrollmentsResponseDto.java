package no.ntnu.idata2306.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Course Enrollments.
 * This class is used to transfer course enrollment data between different layers of the application.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollmentsResponseDto {

    private int id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal credit;
    private int hoursPerWeek;
    private boolean enrolled;
    private LocalDateTime created;
    private CourseResponseDto course;
}
