package no.ntnu.idata2306.util;

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

    private StringUtils(){}
}
