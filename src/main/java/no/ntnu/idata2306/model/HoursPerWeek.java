package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = HoursPerWeek.class)
@JsonIgnoreProperties("courses")
@Schema(description = "hoursPerWeek of a course.", name = "hours_per_week")
@Entity
public class HoursPerWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "hoursPerWeek id")
    private int id;

    @Column(name = "hours", nullable = false, unique = true)
    @Schema(description = "hoursPerWeek hours")
    private int hours;

    @OneToMany(mappedBy = "hoursPerWeek")
    @Schema(description = "courses with the given hoursPerWeek")
    private Set<Course> courses = new LinkedHashSet<>();
}
