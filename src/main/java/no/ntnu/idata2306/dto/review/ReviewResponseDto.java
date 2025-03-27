package no.ntnu.idata2306.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    private int id;
    private int rating;
    private String review;
    private LocalDateTime created;
    private int helpfulVotes;
    private boolean reported;
}
