package no.ntnu.idata2306.util;

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
     * Calculates the average normalized score for a list of words in the parent object's name
     * against a list of words in the search parameter using the Damerau-Levenshtein distance.
     * The score for each word is calculated and the best score for each word in the parent object's name
     * is averaged to get the final score.
     * <p>
     * The method gives extra weight to exact matches by assigning a full score of 100
     * for words that match exactly, and uses the Damerau-Levenshtein distance to calculate the score
     * for partial matches. Additionally, it adjusts the normalized score by adding a bonus for transpositions.
     * </p>
     *
     * @param correctWords the list of words in the parent object's name.
     * @param searchWords the list of words in the search parameter.
     * @return the average normalized score as a percentage.
     */
    public static double calculateAverageNormalizedScore(List<String> correctWords, List<String> searchWords) {
        double totalScore = 0;
        for (String correctWord : correctWords) {
            double bestScore = 0;
            for (String searchWord : searchWords) {
                double score;
                if (correctWord.equalsIgnoreCase(searchWord)) {
                    score = 100; // Exact match gets full score
                } else {
                    int[] distanceAndTranspositions = StringUtils.damerauLevenshteinDistance(searchWord, correctWord);
                    score = adjustedNormalizeScore(distanceAndTranspositions[0], Math.max(searchWord.length(), correctWord.length()), distanceAndTranspositions[1]);
                }
                bestScore = Math.max(bestScore, score);
            }
            totalScore += bestScore;
        }
        return totalScore / correctWords.size();
    }



    /**
     * Adjusts the normalized score based on the Damerau-Levenshtein distance between two strings,
     * with an additional bonus for transpositions.
     * The score is calculated as follows:
     * <ul>
     *   <li>The Damerau-Levenshtein distance is divided by the maximum length of the two strings to get a similarity ratio.</li>
     *   <li>The similarity ratio is subtracted from 1 to get the dissimilarity ratio.</li>
     *   <li>The dissimilarity ratio is multiplied by 100 to convert it to a percentage score.</li>
     *   <li>An additional bonus is added for each transposition to increase the score for common misspellings involving adjacent character swaps.</li>
     * </ul>
     * This method ensures that the score is higher for strings that are more similar, with extra weight given to transpositions.
     *
     * @param distance       the Damerau-Levenshtein distance between the two strings.
     * @param maxLength      the maximum length of the two strings.
     * @param transpositions the number of transpositions between the two strings.
     * @return the adjusted normalized score as a percentage.
     */
    public static double adjustedNormalizeScore(int distance, int maxLength, int transpositions) {
        double baseScore = (1.0 - (double) distance / maxLength) * 100;
        double transpositionBonus = (double)transpositions * 10; // Example bonus for each transposition
        return Math.min(baseScore + transpositionBonus, 100); // Ensure score doesn't exceed 100%
    }

    private ScoreUtils(){}
}
