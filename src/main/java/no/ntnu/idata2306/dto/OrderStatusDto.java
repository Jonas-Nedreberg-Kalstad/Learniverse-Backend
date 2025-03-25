package no.ntnu.idata2306.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.enums.OrderStatusEnum;

/**
 * Data Transfer Object for Order Status.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDto {
    private int id;
    private OrderStatusEnum orderStatus;
}
