package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Schema(description = "Category of a course.", name = "category")
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "category id")
    private int id;

    @Column(name = "category", nullable = false, unique = true)
    @Schema(description = "category name")
    private String category;

    @JsonBackReference
    @OneToMany(mappedBy = "category")
    @Schema(description = "courses with the given category")
    private Set<Course> courses = new LinkedHashSet<>();
}
