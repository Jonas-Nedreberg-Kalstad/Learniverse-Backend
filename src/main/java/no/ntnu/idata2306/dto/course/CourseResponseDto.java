package no.ntnu.idata2306.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.dto.ProviderResponseDto;
import no.ntnu.idata2306.dto.course.details.*;

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
    private double averageRating;
    private int numberOfReviews;

    private ProviderResponseDto provider;
    private CategoryDto category;
    private CreditDto credit;
    private CurrencyDto currency;
    private DifficultyLevelDto difficultyLevel;
    private HoursPerWeekDto hoursPerWeek;
    private Set<TopicDto> topics;
    private Set<RelatedCertificateDto> relatedCertificates;
}
