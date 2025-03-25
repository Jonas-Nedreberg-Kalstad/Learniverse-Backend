package no.ntnu.idata2306.model.payment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import no.ntnu.idata2306.enums.PaymentMethodEnum;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = PaymentMethod.class)
@Schema(description = "Payment method details", name = "payment_method")
@Entity
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Payment method ID")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, unique = true)
    @Schema(description = "Payment method name")
    private PaymentMethodEnum method;

    @OneToMany(mappedBy = "paymentMethod")
    @Schema(description = "Payments associated with the payment method")
    private Set<Payment> payments = new LinkedHashSet<>();
}
