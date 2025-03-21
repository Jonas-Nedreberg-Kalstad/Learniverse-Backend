package no.ntnu.idata2306.repository.course.details;

import no.ntnu.idata2306.model.course.details.RelatedCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RelatedCertificateRepository extends JpaRepository<RelatedCertificate, Integer> {
}
