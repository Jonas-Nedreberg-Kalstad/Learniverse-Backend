package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Schema(description = "Credit of a course.", name = "credit")
@Entity
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "credit id")
    private int id;

    @Column(name = "credit", nullable = false, unique = true, length = 35)
    @Schema(description = "credit name")
    private String credit;

    @JsonBackReference
    @OneToMany(mappedBy = "credit")
    @Schema(description = "courses with the given credit")
    private Set<Course> courses = new LinkedHashSet<>();

    public Credit(String credit) {
        this.credit = credit;
    }

    public Credit() {
    }
}
