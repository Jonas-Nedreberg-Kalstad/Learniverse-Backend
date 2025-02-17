package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payment details", name = "Payment")
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Payment ID")
    private int id;

    @Column(name = "amount", nullable = false)
    @Schema(description = "Payment amount")
    private BigDecimal amount;

    @Column(name = "created", nullable = false)
    @Schema(description = "Creation date of the payment")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "Payment method used")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "payment_card_id", referencedColumnName = "id")
    @JsonManagedReference
    @Schema(description = "Payment card used")
    private PaymentCard paymentCard;
}