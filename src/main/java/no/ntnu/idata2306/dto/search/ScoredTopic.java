package no.ntnu.idata2306.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.model.Topic;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoredTopic {

    private Topic topic;
    private double score;
}
