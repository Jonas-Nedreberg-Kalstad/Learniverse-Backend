package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "created", nullable = false)
    @Schema(description = "course created date")
    private LocalDateTime created;

    @Column(name = "updated", nullable = true)
    @Schema(description = "course updated date")
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "Category of the given course")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "credit_id", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "credit of the given course")
    private Credit credit;

    @ManyToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "currency of the given price")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "difficulty_level_id", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "difficulty level of the given course")
    private DifficultyLevel difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "hours_per_week_id", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "hours per week of the given course")
    private HoursPerWeek hoursPerWeek;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "user that created the course")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "user that updated the course")
    private User updatedBy;

    @OneToMany(mappedBy = "course")
    @JsonBackReference
    @Schema(description = "reviews the course has gotten")
    private Set<Review> reviews = new LinkedHashSet<>();

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

}
