package no.ntnu.idata2306.util;

import no.ntnu.idata2306.dto.search.ScoredItem;
import no.ntnu.idata2306.util.datastructure.BKTree;
import no.ntnu.idata2306.util.datastructure.BKTreeInitializer;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

/**
 * SearchUtils is a utility class that provides methods for sorting and paginating scored items.
 */
public class SearchUtils {

    /**
     * Sorts and paginates a list of scored items based on their scores.
     *
     * @param <T> the type of scored items.
     * @param scoredItems the list of scored items to be sorted and paginated.
     * @param pageable the pagination information.
     * @return a paginated list of scored items sorted by their scores in descending order.
     */
    public static <T extends ScoredItem> List<T> sortAndPaginate(List<T> scoredItems, Pageable pageable) {
        scoredItems.sort(Comparator.comparingDouble(T::getScore).reversed());

        int totalItems = scoredItems.size();
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min((startIndex + pageable.getPageSize()), totalItems);

        return scoredItems.subList(startIndex, endIndex);
    }

    /**
     * Searches for items based on the provided search term and paginates the results.
     * All items with names matching the search term are considered.
     * The scores are calculated using the Levenshtein distance between the search terms and the names of the items.
     * The scores are normalized to a percentage to reflect the closeness of the match.
     * The results are filtered based on predefined score thresholds to ensure relevance.
     * Only unique items with scores exceeding the threshold are added to the list.
     * BK-trees are used to optimize the search process by efficiently finding close matches.
     *
     * @param <T> the type of items to search for.
     * @param <S> the type of scored items.
     * @param searchTerm the search term to search for.
     * @param pageable the pagination information.
     * @param data the data to be searched.
     * @param extractor a function that extracts the string representation from the items.
     * @param scorer a function that calculates the score for the items.
     * @param threshold the score threshold to filter the results.
     * @param uniqueIdentifierExtractor a function that extracts a unique identifier from the items.
     * @param scorerConstructor a function that creates a scored item from the item and its score.
     * @return a paginated list of scored items based on the search criteria.
     */
    public static <T, S extends ScoredItem> List<S> genericSearch(
            String searchTerm,
            Pageable pageable,
            List<T> data,
            Function<T, String> extractor,
            ToDoubleFunction<T> scorer,
            double threshold,
            ToIntFunction<T> uniqueIdentifierExtractor,
            BiFunction<T, Double, S> scorerConstructor
    ) {
        BKTree<String> tree = BKTreeInitializer.initializeBKTree(data, extractor);

        List<String> results = tree.search(searchTerm, 20);
        List<S> scoredItems = new ArrayList<>();
        Set<Integer> uniqueIdentifiers = new HashSet<>();

        for (String name : results) {
            data.stream()
                    .filter(i -> extractor.apply(i).equals(name))
                    .forEach(item -> {
                        Integer uniqueIdentifier = uniqueIdentifierExtractor.applyAsInt(item);
                        if (!uniqueIdentifiers.contains(uniqueIdentifier)) {
                            double score = scorer.applyAsDouble(item);
                            if (score > threshold) {
                                scoredItems.add(scorerConstructor.apply(item, score));
                                uniqueIdentifiers.add(uniqueIdentifier);
                            }
                        }
                    });
        }

        return sortAndPaginate(scoredItems, pageable);
    }

    private SearchUtils(){

    }
}
