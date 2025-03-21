package no.ntnu.idata2306.repository.course.details;

import no.ntnu.idata2306.model.course.details.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /**
     * Finds categories with a calculated relevance score based on the provided search criteria.
     * The score is calculated as follows:
     * <ul>
     *   <li>10 points if the category name matches the provided category name.</li>
     * </ul>
     * The results are ordered by the calculated score in descending order.
     *
     * The `countQuery` is used to efficiently calculate the total number of records that match the search criteria.
     * This is necessary for determining the total number of pages in the paginated results.
     *
     * @param categoryName the name of the category to search for. Partial matches are allowed.
     * @param pageable     the pagination information.
     * @return a page of objects where each object is an array containing the category and its calculated score.
     *         The results are ordered by the score in descending order.
    */
    @Query(value = "SELECT c.*, " +
            "CASE WHEN c.category LIKE %:categoryName% THEN 10 ELSE 0 END AS score " +
            "FROM Category c " +
            "WHERE c.category LIKE %:categoryName% " +
            "ORDER BY score DESC",
            countQuery = "SELECT COUNT(*) FROM Category c WHERE c.category LIKE %:categoryName%",
            nativeQuery = true)
    Page<Object[]> findCategoriesWithScore(@Param("categoryName") String categoryName, Pageable pageable);




}
