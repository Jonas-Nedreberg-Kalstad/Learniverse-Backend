package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.service.OrderService;
import no.ntnu.idata2306.service.ReceiptService;
import no.ntnu.idata2306.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@CrossOrigin()
@RequestMapping("/api")
@Tag(name = "Receipt API", description = "Endpoints for receipt generation")
public class ReceiptController {

    private final ReceiptService receiptService;
    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public ReceiptController(ReceiptService receiptService, UserService userService, OrderService orderService) {
        this.receiptService = receiptService;
        this.userService = userService;
        this.orderService = orderService;
    }

    /**
     * Generates a PDF receipt for a specific order.
     *
     * @param orderId the ID of the order for which to generate a receipt
     * @return ResponseEntity with the PDF receipt
     */
    @Operation(summary = "Get receipt PDF", description = "Generates a PDF receipt for a specific order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receipt generated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/receipts/{orderId}")
    public ResponseEntity<byte[]> getReceiptPdf(@PathVariable int orderId) {
        User user = userService.getSessionUser();

        // Verify that the order exists and belongs to the user
        if (!orderService.isOrderOwnedByUser(orderId, user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Generate the receipt
        byte[] pdfContent = receiptService.generateReceiptPdf(orderId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "receipt-" + orderId + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        log.info("Receipt PDF generated successfully for order ID: {}", orderId);
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }

    /**
     * Generates a PDF receipt for a specific order (admin endpoint).
     *
     * @param orderId the ID of the order for which to generate a receipt
     * @return ResponseEntity with the PDF receipt
     */
    @Operation(summary = "Get receipt PDF (Admin)", description = "Generates a PDF receipt for a specific order (admin access).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receipt generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order ID"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/receipts/{orderId}")
    public ResponseEntity<byte[]> getReceiptPdfAdmin(@PathVariable int orderId) {

        byte[] pdfContent = receiptService.generateReceiptPdf(orderId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "receipt-" + orderId + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        log.info("Receipt PDF generated successfully by admin for order ID: {}", orderId);
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
}