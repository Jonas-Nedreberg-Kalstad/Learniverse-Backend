package no.ntnu.idata2306.repository;

import no.ntnu.idata2306.model.HoursPerWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoursPerWeekRepository extends JpaRepository<HoursPerWeek, Integer> {
}
