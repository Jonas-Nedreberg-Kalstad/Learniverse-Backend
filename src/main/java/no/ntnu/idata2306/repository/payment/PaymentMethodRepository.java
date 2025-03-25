package no.ntnu.idata2306.repository.payment;

import no.ntnu.idata2306.enums.PaymentMethodEnum;
import no.ntnu.idata2306.model.payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    PaymentMethod findByMethod(PaymentMethodEnum method);
}
