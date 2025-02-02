package no.ntnu.idata2306.model;

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
@Schema(description = "Order status details", name = "order_status")
@Entity
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Order status ID")
    private int id;

    @Column(name = "status", nullable = false, unique = true)
    @Schema(description = "Order status")
    private String status;

    @OneToMany(mappedBy = "orderStatus")
    @Schema(description = "Orders associated with the order status")
    private Set<Orders> orders = new LinkedHashSet<>();
}