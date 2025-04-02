package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.order.OrderPaymentDto;
import no.ntnu.idata2306.dto.order.OrderResponseDto;
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

import java.util.List;

@Slf4j
@Validated
@RestController
@CrossOrigin()
@RequestMapping("/api")
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
    @PostMapping(value = "/user/orders/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderPaymentDto orderPaymentDto) {
        User user = this.userService.getSessionUser();
        OrderResponseDto createdOrder = this.orderService.processOrderPaymentWithDebitCard(orderPaymentDto, user);
        log.info("Order created with ID: {}", createdOrder.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }


    /**
     * Retrieves all orders.
     * Error code 500 is handled by global exception handler.
     *
     * @return a list of OrderResponseDto objects representing all orders.
     */
    @Operation(summary = "Get all orders", description = "Retrieves a list of all orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/orders")
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * Retrieves an order by its ID.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the order to retrieve
     * @return ResponseEntity with the OrderResponseDto object and HTTP status
     */
    @Operation(summary = "Get order by ID", description = "Retrieves an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/orders/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable int id) {
        OrderResponseDto order = orderService.getOrderById(id);
        log.info("Order found with ID: {}", id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Retrieves order history for a given user.
     * Error code 500 is handled by global exception handler.
     *
     * @return a list of OrderResponseDto objects representing the user's order history.
     */
    @Operation(summary = "Get all orders for a user", description = "Retrieves all orders for a given user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/orders/history")
    public List<OrderResponseDto> getOrdersHistory() {
        User user = userService.getSessionUser();
        return orderService.getOrdersHistory(user);
    }


}
