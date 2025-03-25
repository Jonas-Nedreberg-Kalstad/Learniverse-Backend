package no.ntnu.idata2306.model.payment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import no.ntnu.idata2306.model.User;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = PaymentCard.class)
@Schema(description = "Payment card details", name = "payment_card")
@Entity
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Payment card ID")
    private int id;

    @Column(name = "card_token", nullable = false, unique = true)
    @Schema(description = "Card token")
    private String cardToken;

    @Column(name = "last_four_digits", nullable = false)
    @Schema(description = "Last four digits of the card")
    private String lastFourDigits;

    @Column(name = "expiration_date", nullable = false)
    @Schema(description = "Expiration date of the card")
    private LocalDateTime expirationDate;

    @Column(name = "created", nullable = false)
    @Schema(description = "Creation date of the payment card")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Schema(description = "User associated with the payment card")
    private User user;

    @OneToMany(mappedBy = "paymentCard")
    @Schema(description = "Payments with the given payment card")
    private Set<Payment> payments = new LinkedHashSet<>();
}
