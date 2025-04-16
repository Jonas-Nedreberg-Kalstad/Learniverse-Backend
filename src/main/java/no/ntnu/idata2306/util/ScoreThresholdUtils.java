package no.ntnu.idata2306.util;

/**
 * Utility class for defining score thresholds used in various search operations.
 * These thresholds are used to filter search results based on their relevance scores.
 */
public class ScoreThresholdUtils {

    /**
     * The score threshold for courses. Only courses with a score above this threshold are considered relevant.
     * Set relatively low to include results with partial word matches and different word orders.
     */
    public static final double COURSE_SCORE_THRESHOLD = 20.0;

    /**
     * The score threshold for categories. Only categories with a score above this threshold are considered relevant.
     * Categories often have shorter names
     */
    public static final double CATEGORY_SCORE_THRESHOLD = 20.0;

    /**
     * The score threshold for topics. Only topics with a score above this threshold are considered relevant.
     * Topics may have word order variations, but we still want reasonable matches.
     */
    public static final double TOPIC_SCORE_THRESHOLD = 20.0;

    /**
     * The score threshold for users. Only users with a score above this threshold are considered relevant.
     * Usernames typically have first/last name order variations
     */
    public static final double USER_SCORE_THRESHOLD = 30.0;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ScoreThresholdUtils() {}
}
