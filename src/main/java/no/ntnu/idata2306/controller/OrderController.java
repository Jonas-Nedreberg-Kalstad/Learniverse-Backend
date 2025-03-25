package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.OrderPaymentDto;
import no.ntnu.idata2306.dto.OrderResponseDto;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.model.payment.Orders;
import no.ntnu.idata2306.service.OrderService;
import no.ntnu.idata2306.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@CrossOrigin()
@RequestMapping("/api/user/orders")
@Tag(name = "Order API", description = "Endpoints for orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    /**
     * Creates a new order using the provided payment details and user information.
     * Error code 400, 422 and 500 is handled by global exception handler.
     *
     * @param orderPaymentDto the DTO containing order payment information
     * @return ResponseEntity with the created Orders object and HTTP status
     */
    @Operation(summary = "Create a new order", description = "Creates a new order with the provided payment details and user information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "422", description = "Error occurred while hashing the card token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderPaymentDto orderPaymentDto) {
        User user = this.userService.getSessionUser();
        OrderResponseDto createdOrder = this.orderService.processOrderPaymentWithDebitCard(orderPaymentDto, user);
        log.info("Order created with ID: {}", createdOrder.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

}
