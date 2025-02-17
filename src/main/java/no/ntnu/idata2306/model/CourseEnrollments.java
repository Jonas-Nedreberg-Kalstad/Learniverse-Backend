package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Course enrollment details", name = "course_enrollments")
@Entity
public class CourseEnrollments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Course enrollment ID")
    private int id;

    @Column(name = "start_date", nullable = false)
    @Schema(description = "Start date of course")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    @Schema(description = "End date of course")
    private LocalDateTime endDate;

    @Column(name = "credit", nullable = false)
    @Schema(description = "Credit for the course")
    private BigDecimal credit;

    @Column(name = "hours_per_week", nullable = false)
    @Schema(description = "Hours per week for the course")
    private int hoursPerWeek;

    @Column(name = "enrollment_status", nullable = false)
    @Schema(description = "Enrollment status")
    private String enrollmentStatus;

    @Column(name = "created", nullable = false)
    @Schema(description = "Creation date of the enrollment")
    private LocalDateTime created;

    @Column(name = "updated", nullable = true)
    @Schema(description = "Update date of the enrollment")
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "Course associated with the enrollment")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "User associated with the enrollment")
    private User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "orders_id", referencedColumnName = "id")
    @Schema(description = "Order associated with the enrollment")
    private Orders orders;
}