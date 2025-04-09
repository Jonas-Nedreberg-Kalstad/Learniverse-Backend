package no.ntnu.idata2306.util;

import no.ntnu.idata2306.dto.LevenshteinDto;

import java.util.List;

/**
 * Utility class for scoring calculations.
 */
public class ScoreUtils {
    
    /**
     * Normalizes the score based on the Levenshtein distance between two strings.
     * The score is calculated as follows:
     * <ul>
     *   <li>The Levenshtein distance is divided by the maximum length of the two strings to get a similarity ratio.</li>
     *   <li>The similarity ratio is subtracted from 1 to get the dissimilarity ratio.</li>
     *   <li>The dissimilarity ratio is multiplied by 100 to convert it to a percentage score.</li>
     * </ul>
     * This method ensures that the score is higher for strings that are more similar.
     *
     * @param distance  the Levenshtein distance between the two strings.
     * @param maxLength the maximum length of the two strings.
     * @return the normalized score as a percentage.
     */
    public static double normalizeScore(int distance, int maxLength) {
        return (1.0 - (double) distance / maxLength) * 100;
    }

    /**
     * Calculates the similarity score between a list of words and a search term.
     * This method:
     * 1. Assigns full scores to exact matches
     * 2. Handles phrase order variations with appropriate scoring
     * 3. Provides partial scores for misspelled words
     * 4. Treats all words equally regardless of length
     *
     * @param correctWords the list of words to compare against (e.g., from database items).
     * @param searchWords the list of words from the search term.
     * @return the similarity score as a percentage.
     */
    public static double calculateSimilarityScore(List<String> correctWords, List<String> searchWords) {
        if (correctWords.isEmpty() || searchWords.isEmpty()) {
            return 0.0;
        }
        
        List<String> correctLowercase = correctWords.stream()
            .map(String::toLowerCase)
            .toList();
        
        List<String> searchLowercase = searchWords.stream()
            .map(String::toLowerCase)
            .toList();
        
        String correctPhrase = String.join(" ", correctLowercase);
        String searchPhrase = String.join(" ", searchLowercase);
        
        if (correctPhrase.equalsIgnoreCase(searchPhrase)) {
            return 100.0;  // Perfect match gets full score
        }
        
        if (correctLowercase.size() == 1 && searchLowercase.size() == 1) {
            String correctWord = correctLowercase.get(0);
            String searchWord = searchLowercase.get(0);
            
            // For single words, use Levenshtein distance directly
            LevenshteinDto dto = StringUtils.damerauLevenshteinDistance(correctWord, searchWord);
            int maxLength = Math.max(correctWord.length(), searchWord.length());
            return normalizeScore(dto.getDistance(), maxLength);
        }

        // 40% weight for whole phrase comparison (preserves order and completeness)
        LevenshteinDto phraseDto = StringUtils.damerauLevenshteinDistance(correctPhrase, searchPhrase);
        int phraseMaxLength = Math.max(correctPhrase.length(), searchPhrase.length());
        double phraseScore = normalizeScore(phraseDto.getDistance(), phraseMaxLength);
        
        // 60% weight for word-by-word comparison (handles word order differences)
        double wordByWordScore = calculateWordMatchScore(
            correctLowercase.toArray(new String[0]), 
            searchLowercase.toArray(new String[0])
        );
        
        return (0.4 * phraseScore) + (0.6 * wordByWordScore);
    }
    
    /**
     * Calculates match score between two word sets regardless of order.
     * Treats all words equally without filtering by length or using thresholds.
     * 
     * @param correctWords array of words from the correct phrase
     * @param searchWords array of words from the search phrase
     * @return similarity score as a percentage
     */
    private static double calculateWordMatchScore(String[] correctWords, String[] searchWords) {
        if (correctWords.length == 0 || searchWords.length == 0) {
            return 0.0;
        }
        
        double totalScore = 0;
        boolean[] matchedCorrect = new boolean[correctWords.length];
        
        for (String searchWord : searchWords) {
            double bestMatchScore = 0;
            int bestMatchIndex = -1;
            
            // Find best match for this search word
            int j = 0;
            boolean searching = true;
            while (searching && j < correctWords.length) {
                if (!matchedCorrect[j]) { // Only process unmatched words
                    double score;
                    if (searchWord.equals(correctWords[j])) {
                        // Exact matches get full score
                        score = 100.0;
                        bestMatchScore = score;
                        bestMatchIndex = j;
                        searching = false;
                    } else {
                        // Calculate similarity
                        score = calculateWordSimilarity(searchWord, correctWords[j]);
                        if (score > bestMatchScore) {
                            bestMatchScore = score;
                            bestMatchIndex = j;
                        }
                    }
                }
                j++;
            }
            
            // Add best match score to total
            totalScore += bestMatchScore;
            
            // Mark best match as used if found
            if (bestMatchIndex >= 0) {
                matchedCorrect[bestMatchIndex] = true;
            }
        }
        return totalScore / searchWords.length;
    }

    /**
     * Calculates similarity between two words without any length-based filtering.
     * 
     * @param word1 the first word
     * @param word2 the second word
     * @return similarity score as a percentage
     */
    private static double calculateWordSimilarity(String word1, String word2) {
        if (word1.equals(word2)) {
            return 100.0;  // Exact match
        }
        
        if (word1.contains(word2) || word2.contains(word1)) {
            int minLength = Math.min(word1.length(), word2.length());
            int maxLength = Math.max(word1.length(), word2.length());
            return (minLength / (double) maxLength) * 95;
        }

        LevenshteinDto dto = StringUtils.damerauLevenshteinDistance(word1, word2);
        int maxLength = Math.max(word1.length(), word2.length());
        return normalizeScore(dto.getDistance(), maxLength);
    }

    private ScoreUtils(){}
}
