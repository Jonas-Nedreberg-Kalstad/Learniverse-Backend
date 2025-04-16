package no.ntnu.idata2306.repository.course;

import no.ntnu.idata2306.model.course.CourseEnrollments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseEnrollmentsRepository extends JpaRepository<CourseEnrollments, Integer> {
    List<CourseEnrollments> findByUserId(int userId);
}
