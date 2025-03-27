package no.ntnu.idata2306.dto.review;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank(message = "Review text cannot be blank")
    @Size(max = 1024, message = "Review text must be at most 1024 characters")
    private String review;

    @NotNull(message = "Course ID cannot be null")
    private int courseId;

}
