package no.ntnu.idata2306.repository;

import no.ntnu.idata2306.model.Category;
import no.ntnu.idata2306.model.Course;
import no.ntnu.idata2306.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    Page<Course> findByDifficultyLevelId(Integer difficultyLevelId, Pageable pageable);

    Page<Course> findByActiveAndCategory(Boolean active, Category category, Pageable pageable);

    Page<Course> findByTopicsIn(Collection<Set<Topic>> topics, Pageable pageable);

    List<Course> findByActiveTrue();
}