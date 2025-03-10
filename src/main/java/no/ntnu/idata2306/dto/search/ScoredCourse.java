package no.ntnu.idata2306.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.model.Course;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoredCourse {

    private Course course;
    private double score;
}
