package no.ntnu.idata2306.repository.course;

import no.ntnu.idata2306.dto.course.CourseResponseDto;
import no.ntnu.idata2306.dto.search.response.ScoredCourse;
import no.ntnu.idata2306.mapper.course.CourseMapper;
import no.ntnu.idata2306.model.course.Course;
import no.ntnu.idata2306.util.ScoreUtils;
import no.ntnu.idata2306.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

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
                CourseResponseDto courseDto = CourseMapper.INSTANCE.courseToResponseCourseDto(course);
                matchedTopics.add(new ScoredCourse(courseDto, score));
            }
        }
        return matchedTopics;
    }

    /**
     * Searches for courses based on the provided category ID, difficulty level ID, list of topic IDs, and maximum price.
     * The search includes only active courses. Any of the parameters can be null, making them optional in the search criteria.
     * The results are fetched eagerly to include related topics in the same query.
     *
     * The query performs the following:
     * - Selects courses from the Course entity and eagerly fetches related topics using JOIN FETCH.
     * - Filters courses based on the provided category ID if it's not null.
     * - Filters courses based on the provided difficulty level ID if it's not null.
     * - Ensures only active courses are included in the results.
     * - Filters courses based on the provided maximum price.
     * - Uses a sub query to filter courses based on the provided topic IDs. The sub query selects course IDs and joins topics,
     *   filtering by the provided list of topic IDs.
     *   It groups results by course ID and checks that the number of distinct topics matches the size of the provided topic IDs list,
     *   ensuring each course has all specified topics.
     *
     * @param categoryId the ID of the category to search for. Can be null.
     * @param topicIds   the list of topic IDs to search for. Can be null.
     * @param difficultyLevelId the ID of the difficulty level to search for. Can be null.
     * @param maxPrice   the maximum price of the courses to search for. Can be null.
     * @param pageable   the pagination information.
     * @return a page of courses that match the search criteria.
     */
    @Query("SELECT DISTINCT course FROM Course course " +
            "LEFT JOIN FETCH course.topics t " +
            "WHERE (:categoryId IS NULL OR course.category.id = :categoryId) " +
            "AND (:difficultyLevelId IS NULL OR course.difficultyLevel.id = :difficultyLevelId) " +
            "AND (course.active = true) " +
            "AND (:maxPrice IS NULL OR course.price <= :maxPrice) " +
            "AND (:topicIds IS NULL OR course.id IN " +
            "(SELECT c.id FROM Course c JOIN c.topics t1 WHERE t1.id IN :topicIds GROUP BY c.id HAVING COUNT(DISTINCT t1.id) = :#{#topicIds == null ? 0 : #topicIds.size()}))")
    Page<Course> searchCoursesByTopicsAndCategory(@Param("categoryId") Integer categoryId,
                                                  @Param("topicIds") List<Integer> topicIds,
                                                  @Param("difficultyLevelId") Integer difficultyLevelId,
                                                  @Param("maxPrice") BigDecimal maxPrice,
                                                  Pageable pageable);

    /**
     * Finds a list of courses by their provider ID.
     *
     * @param providerId the ID of the provider the course(s) belongs to.
     * @return a list of courses.
     */
    List<Course> findCoursesByProviderId(Integer providerId);

    /**
     * Finds all active courses.
     *
     * @return a list of active courses.
     */
    List<Course> findByActiveTrue();

    /**
     * Finds all active courses with pagination.
     *
     * @param pageable the pagination information.
     * @return a page of active courses.
     */
    Page<Course> findByActiveTrue(Pageable pageable);
}