package no.ntnu.idata2306.model.course.details;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import no.ntnu.idata2306.model.course.Course;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Currency.class)
@Schema(description = "Currency of a course.", name = "currency")
@Entity
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "currency id")
    private int id;

    @Column(name = "currency", nullable = false, unique = true, length = 35)
    @Schema(description = "currency name")
    private String currency;

    @OneToMany(mappedBy = "currency")
    @Schema(description = "courses with the given currency")
    private Set<Course> courses = new LinkedHashSet<>();
}