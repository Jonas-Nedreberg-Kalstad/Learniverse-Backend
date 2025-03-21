package no.ntnu.idata2306.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.dto.course.details.TopicDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoredTopic implements ScoredItem {

    private TopicDto topic;
    private double score;

    @Override
    public double getScore() {
        return score;
    }

}
