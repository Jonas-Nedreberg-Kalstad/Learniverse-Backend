package no.ntnu.idata2306.repository.payment;

import no.ntnu.idata2306.model.payment.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Integer> {
}
