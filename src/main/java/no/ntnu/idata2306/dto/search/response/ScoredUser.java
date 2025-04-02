package no.ntnu.idata2306.dto.search.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.dto.search.ScoredItem;
import no.ntnu.idata2306.dto.user.UserResponseDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoredUser implements ScoredItem {

    private UserResponseDto user;
    private double score;

    @Override
    public double getScore() {
        return score;
    }
}
