package no.ntnu.idata2306.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Dto for to be sent as a response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDto {

    private int id;
    private String courseName;
    private BigDecimal price;
    private String description;
    private String requirementDescription;
    private String courseUrl;
    private String courseImageUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime created;
    private LocalDateTime updated;
    private boolean active;
    private Category category;
    private Credit credit;
    private Currency currency;
    private DifficultyLevel difficultyLevel;
    private HoursPerWeek hoursPerWeek;
    private Set<Topic> topics;
    private Set<RelatedCertificate> relatedCertificates;
}
