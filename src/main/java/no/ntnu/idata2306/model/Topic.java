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
@Schema(description = "Topic which courses may have.", name = "topic")
@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "topic id")
    private int id;

    @Column(name = "topic", nullable = false, unique = true)
    @Schema(description = "topic name")
    private String topic;

    @JsonBackReference
    @ManyToMany(mappedBy = "topics")
    @Schema(description = "Courses with the given topic(s)")
    private Set<Course> courses = new LinkedHashSet<>();
}