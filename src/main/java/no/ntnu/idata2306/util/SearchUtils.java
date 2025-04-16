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

    // Limit for fuzzy matches during BKTree search
    private static final int FUZZY_MATCH_LIMIT = 30;

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

        if (startIndex >= totalItems) {
            return Collections.emptyList();
        }
        
        return scoredItems.subList(startIndex, endIndex);
    }

    /**
     * Performs an enhanced fuzzy search using a hybrid approach combining Damerau-Levenshtein distance 
     * and unordered search. This method allows full control over how similarity scores are calculated.
     * 
     * The search process works as follows:
     * A BK-Tree is initialized with the data to enable efficient fuzzy searching
     * Exact matches are identified and given the highest priority (score of 100)
     * The BK-Tree's hybrid search is used to find and sort potential matches by similarity
     * For each candidate, a custom similarity score is calculated using the provided scorer function
     * Only items with scores above the threshold are included in the results are sorted by score and paginated
     *
     * @param <T> the type of items to search for.
     * @param <S> the type of scored items.
     * @param searchTerm the search term to search for.
     * @param pageable the pagination information.
     * @param data the data to be searched.
     * @param extractor a function that extracts the string representation from the items.
     * @param scorer a function that calculates the similarity score between the search term and each item.
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
        if (searchTerm == null || searchTerm.trim().isEmpty() || data == null || data.isEmpty()) {
            return Collections.emptyList();
        }
        
        String normalizedSearchTerm = searchTerm.toLowerCase().trim();
        List<S> scoredItems = new ArrayList<>();
        Set<Integer> uniqueIdentifiers = new HashSet<>();
        
        // Direct search for exact matches
        boolean foundExactMatch = findExactMatches(
            data, 
            normalizedSearchTerm, 
            extractor, 
            uniqueIdentifierExtractor, 
            scorerConstructor, 
            scoredItems, 
            uniqueIdentifiers
        );
        
        // If we didn't find exact matches, try fuzzy matching through the BKTree
        if (!foundExactMatch || scoredItems.size() < pageable.getPageSize()) {
            // Use BK-Tree hybrid search for fuzzy matching
            fuzzyMatchWithBKTree(
                data,
                normalizedSearchTerm,
                extractor,
                scorer,
                threshold,
                uniqueIdentifierExtractor,
                scorerConstructor,
                scoredItems,
                uniqueIdentifiers
            );
        }
        
        return sortAndPaginate(scoredItems, pageable);
    }
    
    /**
     * Finds exact matches for the search term in the data and adds them to the scored items list.
     * 
     * @param data the list of items to search through
     * @param normalizedSearchTerm the search term in normalized form
     * @param extractor function to extract searchable text from an item
     * @param uniqueIdentifierExtractor function to extract unique IDs from items
     * @param scorerConstructor function to create a scored item
     * @param scoredItems the list to add scored matches to
     * @param uniqueIdentifiers set of unique IDs to avoid duplicates
     * @return true if any exact matches were found
     */
    private static <T, S extends ScoredItem> boolean findExactMatches(
            List<T> data,
            String normalizedSearchTerm,
            Function<T, String> extractor,
            ToIntFunction<T> uniqueIdentifierExtractor,
            BiFunction<T, Double, S> scorerConstructor,
            List<S> scoredItems,
            Set<Integer> uniqueIdentifiers
    ) {
        boolean foundMatch = false;
        
        int i = 0;
        while (i < data.size()) {
            T item = data.get(i);
            String itemText = extractor.apply(item).toLowerCase();
            Integer uniqueIdentifier = uniqueIdentifierExtractor.applyAsInt(item);
            
            if (!uniqueIdentifiers.contains(uniqueIdentifier) && itemText.equalsIgnoreCase(normalizedSearchTerm)) {
                // Handle exact matches - these get perfect 100 score
                scoredItems.add(scorerConstructor.apply(item, 100.0));
                uniqueIdentifiers.add(uniqueIdentifier);
                foundMatch = true;

            }
            i++;
        }
        
        return foundMatch;
    }
    
    /**
     * Uses BKTree to find fuzzy matches for the search term in the data and adds them to the scored items list.
     * 
     * @param data the list of items to search through
     * @param normalizedSearchTerm the search term in normalized form
     * @param extractor function to extract searchable text from an item
     * @param scorer function to calculate similarity scores
     * @param threshold minimum score to include a result
     * @param uniqueIdentifierExtractor function to extract unique IDs from items
     * @param scorerConstructor function to create a scored item
     * @param scoredItems the list to add scored matches to
     * @param uniqueIdentifiers set of unique IDs to avoid duplicates
     */
    private static <T, S extends ScoredItem> void fuzzyMatchWithBKTree(
            List<T> data,
            String normalizedSearchTerm,
            Function<T, String> extractor,
            ToDoubleFunction<T> scorer,
            double threshold,
            ToIntFunction<T> uniqueIdentifierExtractor,
            BiFunction<T, Double, S> scorerConstructor,
            List<S> scoredItems,
            Set<Integer> uniqueIdentifiers
    ) {
        BKTree<String> tree = BKTreeInitializer.initializeBKTree(data, extractor);
        List<String> candidates = tree.hybridSearch(normalizedSearchTerm, FUZZY_MATCH_LIMIT);
        
        for (String candidate : candidates) {
            data.stream()
                .filter(item -> extractor.apply(item).equalsIgnoreCase(candidate))
                .forEach(item -> {
                    Integer uniqueIdentifier = uniqueIdentifierExtractor.applyAsInt(item);
                    
                    if (!uniqueIdentifiers.contains(uniqueIdentifier)) {
                        // Apply the user-provided scoring function
                        double score = scorer.applyAsDouble(item);
                        
                        if (score >= threshold) {
                            scoredItems.add(scorerConstructor.apply(item, score));
                            uniqueIdentifiers.add(uniqueIdentifier);
                        }
                    }
                });
        }
    }

    private SearchUtils(){
    }
}
