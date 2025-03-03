package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Advertisement.class)
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
    @JoinColumn(name = "agreement_id", referencedColumnName = "id")
    @Schema(description = "Agreement associated with the advertisement")
    private Agreement agreement;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @Schema(description = "Course associated with the advertisement")
    private Course course;

    @OneToMany(mappedBy = "advertisement")
    @Schema(description = "Ad views associated with the advertisement")
    private Set<AdViews> adViews = new LinkedHashSet<>();
}