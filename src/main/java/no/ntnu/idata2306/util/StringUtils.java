package no.ntnu.idata2306.util;

import no.ntnu.idata2306.dto.LevenshteinDto;

/**
 * Utility class for string operations.
 */
public class StringUtils {

    /**
     * Calculates the Levenshtein distance between two strings.
     * The Levenshtein distance is a measure of the difference between two strings.
     * It is defined as the minimum number of single-character edits (insertions, deletions, or substitutions)
     * required to change one string into the other.
     *
     * This method uses dynamic programming to efficiently calculate the distance.
     * The algorithm works as follows:
     * <ul>
     *   <li>Create a 2D array `dp` where `dp[i][j]` represents the Levenshtein distance between the first `i` characters of string `a` and the first `j` characters of string `b`.</li>
     *   <li>Initialize the first row and column of the array. `dp[i][0]` is `i` and `dp[0][j]` is `j` because it takes `i` deletions to convert the first `i` characters of `a` to an empty string, and `j` insertions to convert an empty string to the first `j` characters of `b`.</li>
     *   <li>Fill in the rest of the array using the following recurrence relation:
     *     <ul>
     *       <li>If the characters `a[i-1]` and `b[j-1]` are the same, then `dp[i][j] = dp[i-1][j-1]`.</li>
     *       <li>If the characters are different, then `dp[i][j] = 1 + min(dp[i-1][j-1], dp[i-1][j], dp[i][j-1])`.</li>
     *     </ul>
     *   </li>
     *   <li>The value at `dp[a.length()][b.length()]` is the Levenshtein distance between the two strings.</li>
     * </ul>
     *
     * @param a the first string.
     * @param b the second string.
     * @return the Levenshtein distance between the two strings.
     */
    public static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    /**
     * Calculates the Damerau-Levenshtein distance between two strings.
     * The Damerau-Levenshtein distance is a measure of the difference between two strings.
     * It is defined as the minimum number of single-character edits (insertions, deletions, substitutions, or transpositions)
     * required to change one string into the other.
     *
     * This method uses dynamic programming to efficiently calculate the distance.
     * The algorithm works as follows:
     * <ul>
     *   <li>Create a 2D array `dp` where `dp[i][j]` represents the Damerau-Levenshtein distance between the first `i` characters of string `a` and the first `j` characters of string `b`.</li>
     *   <li>Initialize the first row and column of the array. `dp[i][0]` is `i` and `dp[0][j]` is `j` because it takes `i` deletions to convert the first `i` characters of `a` to an empty string, and `j` insertions to convert an empty string to the first `j` characters of `b`.</li>
     *   <li>Fill in the rest of the array using the following recurrence relation:
     *     <ul>
     *       <li>If the characters `a[i-1]` and `b[j-1]` are the same, then `dp[i][j] = dp[i-1][j-1]`.</li>
     *       <li>If the characters are different, then `dp[i][j] = 1 + min(dp[i-1][j-1], dp[i-1][j], dp[i][j-1])`.</li>
     *       <li>Additionally, if `i > 1` and `j > 1` and the characters `a[i-1]` and `b[j-2]` are the same, and `a[i-2]` and `b[j-1]` are the same, then `dp[i][j] = min(dp[i][j], dp[i-2][j-2] + 1)` to account for transpositions.</li>
     *     </ul>
     *   </li>
     *   <li>The value at `dp[a.length()][b.length()]` is the Damerau-Levenshtein distance between the two strings.</li>
     * </ul>
     *
     * @param a the first string.
     * @param b the second string.
     * @return an object containing the Damerau-Levenshtein distance and the number of transpositions.
     */
    public static LevenshteinDto damerauLevenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        int transpositions = 0;

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + cost,
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));

                    if (i > 1 && j > 1 && a.charAt(i - 1) == b.charAt(j - 2) && a.charAt(i - 2) == b.charAt(j - 1)) {
                        dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                        transpositions++;
                    }
                }
            }
        }
        return new LevenshteinDto(dp[a.length()][b.length()], transpositions);
    }

    /**
     * Converts a given string with underscores to Pascal Case with spaces.
     * Pascal Case is a naming convention in which the first letter of each word is capitalized,
     * and words are separated by spaces.
     *
     * Example:
     * "PAYMENT_COMPLETED" -> "Payment Completed"
     * "PENDING_PAYMENT" -> "Pending Payment"
     *
     * @param input the string to be converted to Pascal Case with spaces.
     * @return the Pascal Case version of the input string with spaces. If the input is null or empty, it returns the input as is.
     */
    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] words = input.split("_");
        StringBuilder pascalCaseString = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                pascalCaseString.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return pascalCaseString.toString().trim();
    }

    private StringUtils(){}
}
