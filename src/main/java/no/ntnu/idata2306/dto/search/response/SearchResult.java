package no.ntnu.idata2306.dto.search.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.dto.search.response.ScoredCategory;
import no.ntnu.idata2306.dto.search.response.ScoredCourse;
import no.ntnu.idata2306.dto.search.response.ScoredTopic;

import java.util.List;

/**
 * Dto for search result
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    private List<ScoredCourse> scoredCourses;
    private List<ScoredCategory> scoredCategories;
    private List<ScoredTopic> scoredTopics;
}
