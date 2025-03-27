package no.ntnu.idata2306.repository;

import no.ntnu.idata2306.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> findByCourseId(int courseId, Pageable pageable);
}
