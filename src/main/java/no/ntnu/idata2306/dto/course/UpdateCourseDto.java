package no.ntnu.idata2306.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.model.course.details.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Dto for updating course
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseDto {

    @NotBlank(message = "Course name cannot be blank")
    @Size(min = 1, max = 150, message = "Course name must be between 1 and 150 characters")
    private String courseName;

    private BigDecimal price;
    private String description;
    private String requirementDescription;
    private String courseUrl;
    private String courseImageUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private Category category;
    private Credit credit;
    private Currency currency;
    private DifficultyLevel difficultyLevel;
    private HoursPerWeek hoursPerWeek;
    private Set<Topic> topics;
    private Set<RelatedCertificate> relatedCertificates;
}
