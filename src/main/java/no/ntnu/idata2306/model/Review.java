package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Review of a course", name = "Review")
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "ID of the review")
    private int id;

    @Column(name = "rating", nullable = false)
    @Schema(description = "Rating given in the review")
    private int rating;

    @Lob // for larger objects
    @Column(name = "review", nullable = false)
    @Schema(description = "Text of the review")
    private String review;

    @Column(name = "created", nullable = false)
    @Schema(description = "The date the review was created")
    private LocalDateTime created;

    @Column(name = "helpful_votes", nullable = false)
    @Schema(description = "Number of helpful votes the review received")
    private int helpfulVotes;

    @Column(name = "reported", nullable = false)
    @Schema(description = "Indicates if the review has been reported")
    private boolean reported;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "User who wrote the review")
    private User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "course_id", nullable = false)
    @Schema(description = "Course that the review is about")
    private Course course;
}
