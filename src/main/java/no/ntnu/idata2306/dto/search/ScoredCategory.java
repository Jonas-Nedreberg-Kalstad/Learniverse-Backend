package no.ntnu.idata2306.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.model.Category;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoredCategory {

    private Category category;
    private double score;
}
