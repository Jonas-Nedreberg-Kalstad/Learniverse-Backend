package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Schema(description = "course of a given provider.", name = "course")
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "course id")
    private int id;

    @Column(name = "course_name", nullable = false)
    @Schema(description = "course name")
    private String courseName;

    @Column(name = "price", nullable = true)
    @Schema(description = "course price")
    private BigDecimal price;

    @Lob // for larger objects
    @Column(name = "description", nullable = true)
    @Schema(description = "course description")
    private String description;

    @Lob // for larger objects
    @Column(name = "requirement_description", nullable = true)
    @Schema(description = "course requirement description")
    private String requirementDescription;

    @Column(name = "course_url", nullable = true)
    @Schema(description = "course url")
    private String courseUrl;

    @Column(name = "course_image_url", nullable = true)
    @Schema(description = "course image url")
    private String courseImageUrl;

    @Column(name = "start_date", nullable = true)
    @Schema(description = "course start date")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = true)
    @Schema(description = "course end date")
    private LocalDateTime endDate;

    @Column(name = "active", nullable = true)
    @Schema(description = "course is active")
    private boolean active;

    @JsonManagedReference
    @ManyToOne()
    @JoinColumns(
            @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    @Schema(description = "category of the given course")
    private Category category;

    @JsonManagedReference
    @ManyToOne()
    @JoinColumns(
            @JoinColumn(name = "credit_id", referencedColumnName = "id")
    )
    @Schema(description = "credit of the given course")
    private Credit credit;

    @JsonManagedReference
    @ManyToOne()
    @JoinColumns(
            @JoinColumn(name = "currency_id", referencedColumnName = "id")
    )
    @Schema(description = "currency of the given price")
    private Currency currency;

    @JsonManagedReference
    @ManyToOne()
    @JoinColumns(
            @JoinColumn(name = "difficulty_level_id", referencedColumnName = "id")
    )
    @Schema(description = "difficulty level of the given course")
    private DifficultyLevel difficultyLevel;

    @JsonManagedReference
    @ManyToOne()
    @JoinColumns(
            @JoinColumn(name = "hours_per_week_id", referencedColumnName = "id")
    )
    @Schema(description = "hours per week of the given course")
    private HoursPerWeek hoursPerWeek;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_topic",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id")
    )
    @Schema(description = "Topic(s) of a given course")
    private Set<Topic> topics = new LinkedHashSet<>();

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_related_certificate",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "related_certificate_id", referencedColumnName = "id")
    )
    @Schema(description = "RelatedCertificate(s) of a given course")
    private Set<RelatedCertificate> relatedCertificates = new LinkedHashSet<>();


    /**
     *
     * @param courseName                    courseName
     * @param price                         price
     * @param description                   description
     * @param requirementDescription        requirementDescription
     * @param courseUrl                     courseUrl
     * @param courseImageUrl                courseImageUrl
     * @param startDate                     startDate
     * @param endDate                       endDate
     * @param active                        active
     */
    public Course(String courseName, BigDecimal price, String description, String requirementDescription, String courseUrl, String courseImageUrl, LocalDateTime startDate, LocalDateTime endDate, boolean active) {
        this.courseName = courseName;
        this.price = price;
        this.description = description;
        this.requirementDescription = requirementDescription;
        this.courseUrl = courseUrl;
        this.courseImageUrl = courseImageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public Course(){

    }
}
