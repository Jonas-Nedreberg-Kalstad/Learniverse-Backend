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
@Schema(description = "Advertisement details", name = "advertisement")
@Entity
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Advertisement ID")
    private int id;

    @Column(name = "fee", nullable = false)
    @Schema(description = "Advertisement fee")
    private BigDecimal fee;

    @Column(name = "created", nullable = false)
    @Schema(description = "Creation date of the advertisement")
    private LocalDateTime created;

    @Column(name = "updated", nullable = true)
    @Schema(description = "Update date of the advertisement")
    private LocalDateTime updated;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "agreement_id", referencedColumnName = "id")
    @Schema(description = "Agreement associated with the advertisement")
    private Agreement agreement;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @Schema(description = "Course associated with the advertisement")
    private Course course;

    @OneToMany(mappedBy = "advertisement")
    @JsonBackReference
    @Schema(description = "ad views associated with the advertisement")
    private Set<AdViews> adViews = new LinkedHashSet<>();
}