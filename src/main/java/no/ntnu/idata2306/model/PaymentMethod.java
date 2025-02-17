package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payment method details", name = "payment_method")
@Entity
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Payment method ID")
    private int id;

    @Column(name = "method", nullable = false, unique = true)
    @Schema(description = "Payment method name")
    private String method;

    @OneToMany(mappedBy = "paymentMethod")
    @JsonBackReference
    @Schema(description = "Payments associated with the payment method")
    private Set<Payment> payments = new LinkedHashSet<>();
}
