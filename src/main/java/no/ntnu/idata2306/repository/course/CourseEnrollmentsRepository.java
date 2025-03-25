package no.ntnu.idata2306.repository.course;

import no.ntnu.idata2306.model.course.CourseEnrollments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollmentsRepository extends JpaRepository<CourseEnrollments, Integer> {
}
