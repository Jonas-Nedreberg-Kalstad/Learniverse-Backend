package no.ntnu.idata2306.dto.search.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Dto for Searching for multiple topics and a category
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAndTopicsSearch {
    private Integer categoryId;
    private List<Integer> topicIds;
    private Integer difficultyLevelId;
    private BigDecimal maxPrice;
}
