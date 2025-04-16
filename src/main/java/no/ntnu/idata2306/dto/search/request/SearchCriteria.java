package no.ntnu.idata2306.dto.search.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dto for search criteria
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {

    @Size(max = 150, message = "Course name must be 150 characters or less")
    private String courseName;
    @Size(max = 150, message = "Category name must be 150 characters or less")
    private String categoryName;
    @Size(max = 150, message = "Topic name must be 150 characters or less")
    private String topicName;
}
