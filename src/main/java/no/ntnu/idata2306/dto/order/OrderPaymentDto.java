package no.ntnu.idata2306.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Order Payments.
 * Includes validation for null/blank values and unique constraints.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentDto {

    @Positive(message = "Course ID must be greater than 0")
    private int courseId;

    @NotBlank(message = "Card token cannot be blank")
    @Size(min = 1, max = 100, message = "Card token must be between 1 and 100 characters")
    private String cardToken;

    @NotBlank(message = "Last four digits cannot be blank")
    @Size(min = 4, max = 4, message = "Last four digits must be exactly 4 characters")
    private String lastFourDigits;

    @NotNull(message = "Expiration date cannot be null")
    private LocalDateTime cardExpirationDate;

}
