package no.ntnu.idata2306.model.payment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import no.ntnu.idata2306.enums.OrderStatusEnum;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = OrderStatus.class)
@Schema(description = "Order status details", name = "order_status")
@Entity
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Order status ID")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, unique = true)
    @Schema(description = "Order status")
    private OrderStatusEnum status;

    @OneToMany(mappedBy = "orderStatus")
    @Schema(description = "Orders associated with the order status")
    private Set<Orders> orders = new LinkedHashSet<>();
}