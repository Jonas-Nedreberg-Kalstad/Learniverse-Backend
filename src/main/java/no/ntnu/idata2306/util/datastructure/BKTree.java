package no.ntnu.idata2306.util.datastructure;

import no.ntnu.idata2306.util.StringUtils;

import java.util.*;

/**
 * BKTree is a data structure used for efficient fuzzy searching.
 * It organizes elements in a way that allows for quick retrieval of close matches based on edit distance.
 * This implementation includes support for hybrid search combining Damerau-Levenshtein distance for fuzzy matching.
 *
 * @param <T> the type of elements stored in the BKTree.
 */
public class BKTree<T> {
    private Node root;

    /**
     * Constructs an empty BKTree.
     */
    public BKTree() {
        this.root = null;
    }

    /**
     * Adds an element to the BKTree.
     * If the tree is empty, the element becomes the root.
     * Otherwise, the element is added to the appropriate position based on its edit distance from existing elements.
     *
     * @param element the element to add to the BKTree.
     */
    public void add(T element) {
        if (root == null) {
            root = new Node(element);
        } else {
            root.add(element);
        }
    }

    /**
     * Searches for elements in the BKTree that are within a specified edit distance from the query.
     * The search is performed recursively, starting from the root and using Damerau-Levenshtein distance.
     * 
     * The BK-Tree is structured based on edit distance, which makes it efficient for finding
     * candidates that are within a certain edit distance of the query term. This is the primary
     * search mechanism that provides candidate matches for further refinement.
     *
     * @param query     the query element to search for.
     * @param threshold the maximum edit distance allowed for matches.
     * @return a list of elements that are within the specified edit distance from the query.
     */
    public List<T> search(T query, int threshold) {
        List<T> results = new ArrayList<>();
        if (root != null) {
            root.search(query, threshold, results);
        }
        return results;
    }
    
    /**
     * Performs a hybrid search that prioritizes exact term matches.
     *
     * @param query     the query element to search for.
     * @param threshold the maximum edit distance allowed for matches.
     * @return a list of elements sorted by their relevance to the query.
     */
    public List<T> hybridSearch(T query, int threshold) {
        List<T> candidates = search(query, threshold);
        
        if (candidates.isEmpty()) {
            return candidates;
        }
        
        String queryStr = query.toString().toLowerCase();
        List<Map.Entry<T, Double>> scoredCandidates = new ArrayList<>();
        
        for (            T candidate : candidates) {
            String candidateStr = candidate.toString().toLowerCase();
            
            // Calculate base similarity score using Damerau-Levenshtein distance
            int distance = StringUtils.damerauLevenshteinDistance(queryStr, candidateStr).getDistance();
            int maxLength = Math.max(queryStr.length(), candidateStr.length());
            double similarity = (1.0 - (double)distance / maxLength) * 65; // Scale to max 65 (out of 100)
            
            // Exact match gets full score
            if (queryStr.equals(candidateStr)) {
                similarity = 100.0;
            }
            
            // Cap the final score to be within a reasonable range (0-85)
            similarity = Math.min(85.0, similarity);
            
            scoredCandidates.add(Map.entry(candidate, similarity));
                    }
        
        // Sort by score (descending)
        scoredCandidates.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        // Extract just the candidates
        List<T> result = new ArrayList<>();
        for (Map.Entry<T, Double> entry : scoredCandidates) {
            result.add(entry.getKey());
                    }
        
        return result;
    }

    /**
     * Node represents a single node in the BKTree.
     * Each node contains an element and a map of children nodes, where the keys are edit distances.
     */
    private class Node {
        private final T element;
        private final Map<Integer, List<Node>> children;

        /**
         * Constructs a Node with the specified element.
         *
         * @param element the element contained in this node.
         */
        public Node(T element) {
            this.element = element;
            this.children = new HashMap<>();
        }

        /**
         * Adds an element to the subtree rooted at this node.
         * The element is placed in the appropriate position based on its edit distance from this node's element.
         * Using `Map<Integer, List<Node>>` allows for handling multiple nodes at the same distance,
         * ensuring that elements with identical or nearly identical names are correctly added to the tree.
         *
         * @param element the element to add to the subtree.
         */
        public void add(T element) {
            int distance = StringUtils.damerauLevenshteinDistance(this.element.toString(), element.toString()).getDistance();
            List<Node> childList = children.computeIfAbsent(distance, k -> new ArrayList<>());
            childList.add(new Node(element));
        }

        /**
         * Searches for elements in the subtree rooted at this node that are within a specified edit distance from the query.
         * The search is performed recursively, considering nodes within the range of the threshold.
         *
         * @param query     the query element to search for.
         * @param threshold the maximum edit distance allowed for matches.
         * @param results   the list to store matching elements.
         */
        public void search(T query, int threshold, List<T> results) {
            int distance = StringUtils.damerauLevenshteinDistance(this.element.toString(), query.toString()).getDistance();
            if (distance <= threshold) {
                results.add(this.element);
            }
            for (int i = Math.max(0, distance - threshold); i <= distance + threshold; i++) {
                List<Node> childList = children.get(i);
                if (childList != null) {
                    for (Node child : childList) {
                        child.search(query, threshold, results);
                    }
                }
            }
        }
    }
}
