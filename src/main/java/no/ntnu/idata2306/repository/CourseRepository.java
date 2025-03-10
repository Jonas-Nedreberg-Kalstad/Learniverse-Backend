package no.ntnu.idata2306.repository;

import no.ntnu.idata2306.dto.search.ScoredCourse;
import no.ntnu.idata2306.model.Category;
import no.ntnu.idata2306.model.Course;
import no.ntnu.idata2306.model.Topic;
import no.ntnu.idata2306.util.ScoreUtils;
import no.ntnu.idata2306.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Page<Course> findByCourseNameContainingIgnoreCase(String courseName, Pageable pageable);

    Page<Course> findByCategory(Category category, Pageable pageable);

    Page<Course> findByCategoryId(Integer categoryId, Pageable pageable);

    Page<Course> findByTopics(Set<Topic> topics, Pageable pageable);

    @Query("SELECT c FROM Course c JOIN c.topics t WHERE t.id = :topicId")
    Page<Course> findByTopicId(@Param("topicId") Integer topicId, Pageable pageable);

    Page<Course> findByCategoryAndTopics(Category category, Set<Topic> topics, Pageable pageable);

    @Query("SELECT c FROM Course c JOIN c.topics t WHERE c.category.id = :categoryId AND t.id = :topicId")
    Page<Course> findByCategoryIdAndTopicId(@Param("categoryId") Integer categoryId,
                                            @Param("topicId") Integer topicId,
                                            Pageable pageable);

    // Advanced search with multiple optional criteria and pagination
    @Query("SELECT course FROM Course course " +
            "JOIN course.topics t " +
            "WHERE (:courseName IS NULL OR LOWER(course.courseName) LIKE LOWER(CONCAT('%', :courseName, '%'))) " +
            "AND (:categoryId IS NULL OR course.category.id = :categoryId) " +
            "AND (:topicId IS NULL OR t.id = :topicId) " +
            "AND (course.active = true)")
    Page<Course> searchCourses(@Param("courseName") String courseName,
                               @Param("categoryId") Integer categoryId,
                               @Param("topicId") Integer topicId,
                               Pageable pageable);


    /**
     * Finds courses based on the provided course name, category name, and topic name.
     * The search allows for partial matches using the SQL LIKE operator.
     *
     * @param courseName   the name of the course to search for. Partial matches are allowed.
     * @return a list of courses that match the search criteria.
     */
    @Query("SELECT c FROM Course c WHERE c.courseName LIKE %:courseName%")
    List<Course> findCoursesByCourseName(@Param("courseName") String courseName);

    /**
     * Finds courses with a calculated relevance score based on the provided search criteria.
     * The score is calculated as follows:
     * <ul>
     *   <li>10 points if the course name matches the provided course name.</li>
     *   <li>5 points if the category name matches the provided category name.</li>
     *   <li>3 points if any of the topics match the provided topic name.</li>
     * </ul>
     * The results are ordered by the calculated score in descending order.
     *
     * The `countQuery` is used to efficiently calculate the total number of records that match the search criteria.
     * This is necessary for determining the total number of pages in the paginated results.
     *
     * @param courseName   the name of the course to search for. Partial matches are allowed.
     * @param categoryName the name of the category to search for. Partial matches are allowed.
     * @param topicName    the name of the topic to search for. Partial matches are allowed.
     * @param pageable     the pagination information.
     * @return a page of objects where each object is an array containing the course and its calculated score.
     *         The results are ordered by the score in descending order.
     */
    @Query(value = "SELECT c.*, " +
            "CASE WHEN c.name LIKE %:courseName% THEN 10 ELSE 0 END + " +
            "CASE WHEN cat.category LIKE %:categoryName% THEN 5 ELSE 0 END + " +
            "CASE WHEN t.topic LIKE %:topicName% THEN 3 ELSE 0 END AS score " +
            "FROM Course c " +
            "LEFT JOIN Category cat ON c.category_id = cat.id " +
            "LEFT JOIN Course_Topic ct ON c.id = ct.course_id " +
            "LEFT JOIN Topic t ON ct.topic_id = t.id " +
            "WHERE c.name LIKE %:courseName% OR cat.category LIKE %:categoryName% OR t.topic LIKE %:topicName% " +
            "ORDER BY score DESC",
            countQuery = "SELECT COUNT(*) FROM Course c " +
                    "LEFT JOIN Category cat ON c.category_id = cat.id " +
                    "LEFT JOIN Course_Topic ct ON c.id = ct.course_id " +
                    "LEFT JOIN Topic t ON ct.topic_id = t.id " +
                    "WHERE c.name LIKE %:courseName% OR cat.category LIKE %:categoryName% OR t.topic LIKE %:topicName%",
            nativeQuery = true)
    Page<Object[]> findCoursesWithScore(@Param("courseName") String courseName,
                                        @Param("categoryName") String categoryName,
                                        @Param("topicName") String topicName,
                                        Pageable pageable);

    @Query("SELECT c FROM Course c")
    List<Course> findAllCourses();

    default List<ScoredCourse> findCategoriesByFuzzySearch(String searchTerm) {
        List<Course> allCategories = findAllCourses();
        List<ScoredCourse> matchedTopics = new ArrayList<>();

        for (Course course : allCategories) {
            int distance = StringUtils.levenshteinDistance(searchTerm, course.getCourseName());
            double score = ScoreUtils.normalizeScore(distance, Math.max(searchTerm.length(), course.getCourseName().length()));
            if (score > 0) { // Percentage greater than 0 due to lack of data
                matchedTopics.add(new ScoredCourse(course, score));
            }
        }
        return matchedTopics;
    }


    Page<Course> findByDifficultyLevelId(Integer difficultyLevelId, Pageable pageable);

    Page<Course> findByActiveAndCategory(Boolean active, Category category, Pageable pageable);

    Page<Course> findByTopicsIn(Collection<Set<Topic>> topics, Pageable pageable);

    List<Course> findByActiveTrue();
}