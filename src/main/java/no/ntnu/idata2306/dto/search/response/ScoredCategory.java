package no.ntnu.idata2306.dto.search.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.dto.course.details.CategoryDto;
import no.ntnu.idata2306.dto.search.ScoredItem;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoredCategory implements ScoredItem {

    private CategoryDto category;
    private double score;

    @Override
    public double getScore() {
        return score;
    }
}
