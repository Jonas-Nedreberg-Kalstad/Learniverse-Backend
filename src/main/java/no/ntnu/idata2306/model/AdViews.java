package no.ntnu.idata2306.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Advertisement views details", name = "ad_views")
@Entity
public class AdViews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Ad view ID")
    private int id;

    @Column(name = "viewed", nullable = false)
    @Schema(description = "Date and time when the ad was viewed")
    private LocalDateTime viewed;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    @Schema(description = "Advertisement associated with the view")
    private Advertisement advertisement;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Schema(description = "User who viewed the advertisement")
    private User user;
}