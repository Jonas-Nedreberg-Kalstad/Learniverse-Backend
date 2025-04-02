package no.ntnu.idata2306.repository.payment;

import no.ntnu.idata2306.model.payment.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUserId(int userId);
}
