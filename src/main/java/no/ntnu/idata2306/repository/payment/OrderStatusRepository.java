package no.ntnu.idata2306.repository.payment;

import no.ntnu.idata2306.enums.OrderStatusEnum;
import no.ntnu.idata2306.model.payment.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    OrderStatus findByStatus(OrderStatusEnum status);
}
