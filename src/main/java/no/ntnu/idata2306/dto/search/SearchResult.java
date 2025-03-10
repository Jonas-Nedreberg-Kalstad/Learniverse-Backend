package no.ntnu.idata2306.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

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
    private Pageable pageable;
}
