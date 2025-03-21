package no.ntnu.idata2306.repository.course.details;

import no.ntnu.idata2306.model.course.details.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

/**
 * Finds topics with a calculated relevance score based on the provided search criteria.
 * The score is calculated as follows:
 * <ul>
 *   <li>10 points if the topic name matches the provided topic name.</li>
 * </ul>
 * The results are ordered by the calculated score in descending order.
 *
 * The `countQuery` is used to efficiently calculate the total number of records that match the search criteria.
 * This is necessary for determining the total number of pages in the paginated results.
 *
 * @param topicName the name of the topic to search for. Partial matches are allowed.
 * @param pageable  the pagination information.
 * @return a page of objects where each object is an array containing the topic and its calculated score.
 *         The results are ordered by the score in descending order.
 */
    @Query(value = "SELECT t.*, " +
            "CASE WHEN t.topic LIKE %:topicName% THEN 10 ELSE 0 END AS score " +
            "FROM Topic t " +
            "WHERE t.topic LIKE %:topicName% " +
            "ORDER BY score DESC",
            countQuery = "SELECT COUNT(*) FROM Topic t WHERE t.topic LIKE %:topicName%",
            nativeQuery = true)
    Page<Object[]> findTopicsWithScore(@Param("topicName") String topicName, Pageable pageable);


}
