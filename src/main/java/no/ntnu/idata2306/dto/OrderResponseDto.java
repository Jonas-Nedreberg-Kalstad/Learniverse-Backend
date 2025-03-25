package no.ntnu.idata2306.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.idata2306.dto.course.CourseResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Order Response.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private int id;
    private BigDecimal price;
    private LocalDateTime created;
    private LocalDateTime updated;
    private CourseResponseDto course;
    private OrderStatusDto orderStatus;
}
