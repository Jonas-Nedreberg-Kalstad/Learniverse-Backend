package no.ntnu.idata2306.util.datastructure;

import no.ntnu.idata2306.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BKTree is a data structure used for efficient fuzzy searching.
 * It organizes strings in a way that allows for quick retrieval of close matches based on edit distance.
 * This implementation uses the Damerau-Levenshtein distance to measure the similarity between strings.
 */
public class BKTree {
    private Node root;

    /**
     * Constructs an empty BKTree.
     */
    public BKTree() {
        this.root = null;
    }

    /**
     * Adds a word to the BKTree.
     * If the tree is empty, the word becomes the root.
     * Otherwise, the word is added to the appropriate position based on its edit distance from existing words.
     *
     * @param word the word to add to the BKTree.
     */
    public void add(String word) {
        if (root == null) {
            root = new Node(word);
        } else {
            root.add(word);
        }
    }

    /**
     * Searches for words in the BKTree that are within a specified edit distance from the query.
     * The search is performed recursively, starting from the root.
     *
     * @param query     the query word to search for.
     * @param threshold the maximum edit distance allowed for matches.
     * @return a list of words that are within the specified edit distance from the query.
     */
    public List<String> search(String query, int threshold) {
        List<String> results = new ArrayList<>();
        if (root != null) {
            root.search(query, threshold, results);
        }
        return results;
    }

    /**
     * Node represents a single node in the BKTree.
     * Each node contains a word and a map of children nodes, where the keys are edit distances.
     */
    private static class Node {
        private final String word;
        private final Map<Integer, Node> children;

        /**
         * Constructs a Node with the specified word.
         *
         * @param word the word contained in this node.
         */
        public Node(String word) {
            this.word = word;
            this.children = new HashMap<>();
        }

        /**
         * Adds a word to the subtree rooted at this node.
         * The word is placed in the appropriate position based on its edit distance from this node's word.
         *
         * @param word the word to add to the subtree.
         */
        public void add(String word) {
            int[] distanceAndTranspositions = StringUtils.damerauLevenshteinDistance(this.word, word);
            Node child = children.get(distanceAndTranspositions[0]);
            if (child == null) {
                children.put(distanceAndTranspositions[0], new Node(word));
            } else {
                child.add(word);
            }
        }

        /**
         * Searches for words in the subtree rooted at this node that are within a specified edit distance from the query.
         * The search is performed recursively, considering nodes within the range of the threshold.
         *
         * @param query     the query word to search for.
         * @param threshold the maximum edit distance allowed for matches.
         * @param results   the list to store matching words.
         */
        public void search(String query, int threshold, List<String> results) {
            int[] distanceAndTranspositions = StringUtils.damerauLevenshteinDistance(this.word, query);
            if (distanceAndTranspositions[0] <= threshold) {
                results.add(this.word);
            }
            for (int i = Math.max(0, distanceAndTranspositions[0] - threshold); i <= distanceAndTranspositions[0] + threshold; i++) {
                Node child = children.get(i);
                if (child != null) {
                    child.search(query, threshold, results);
                }
            }
        }
    }
}
