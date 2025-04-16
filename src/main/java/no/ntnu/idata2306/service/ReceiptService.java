package no.ntnu.idata2306.service;

import com.lowagie.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.exception.ReceiptGenerationException;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.model.payment.Orders;
import no.ntnu.idata2306.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ReceiptService {

    private final OrderService orderService;
    private final TemplateEngine templateEngine;

    @Autowired
    public ReceiptService(OrderService orderService, TemplateEngine templateEngine) {
        this.orderService = orderService;
        this.templateEngine = templateEngine;
    }

    /**
     * Generates a receipt PDF for a given order.
     *
     * @param orderId the ID of the order for which to generate a receipt
     * @return byte array containing the PDF content
     * @throws EntityNotFoundException if the order is not found
     * @throws ReceiptGenerationException if the PDF generation fails
     */
    public byte[] generateReceiptPdf(int orderId) {
        try {
            // Get the order details
            Orders order = orderService.findOrderById(orderId);
            User user = order.getUser();

            return generateReceiptPdf(order, user);
        } catch (DocumentException e) {
            log.error("Failed to generate receipt for order ID: {}", orderId, e);
            throw new ReceiptGenerationException("Failed to generate receipt: " + e.getMessage());
        }
    }

    /**
     * Generates a PDF receipt for the given order.
     *
     * @param order the order for which to generate the receipt
     * @param user the user who placed the order
     * @return byte array containing the PDF content
     * @throws DocumentException if the PDF generation fails
     */
    private byte[] generateReceiptPdf(Orders order, User user) throws DocumentException {
        Context context = new Context();
        Map<String, Object> templateData = prepareTemplateData(order, user);
        context.setVariables(templateData);
        String htmlContent = templateEngine.process("receipt-template", context);

        // Generate PDF from HTML
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);

        return outputStream.toByteArray();
    }

    /**
     * Prepares the data to be used in the receipt template.
     *
     * @param order the order data
     * @param user the user data
     * @return a map containing the data for the template
     */
    private Map<String, Object> prepareTemplateData(Orders order, User user) {
        Map<String, Object> data = new HashMap<>();
        String username = user != null ? user.getFirstName() + " " + user.getLastName() : "Valued Customer";

        data.put("orderId", order.getId());
        data.put("username", username);
        data.put("currentDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        data.put("orderDate", order.getCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        data.put("orderPaymentMethod", StringUtils.toPascalCase(order.getPayment().getPaymentMethod().getMethod().toString()));
        data.put("orderStatus", StringUtils.toPascalCase(order.getOrderStatus().getStatus().toString()));
        data.put("courseName", order.getCourse().getCourseName());
        data.put("coursePrice", order.getPrice());
        data.put("currency", order.getCurrency());
        
        return data;
    }

    /**
     * Creates a user-friendly email body for the receipt email.
     *
     * @param order the order details
     * @param user the user to whom the email is being sent
     * @return the email body as a string
     */
    public String createReceiptEmailBody(Orders order, User user) {
        String userName = user != null ? 
            user.getFirstName() + " " + user.getLastName() : "Valued Customer";

        return String.format(
                "<html><body>" +
                        "<p>Dear %s,</p>" +
                        "<p>Thank you for your purchase! We're excited that you've chosen our platform for your learning journey.</p>" +
                        "<p>Order Details:<br>" +
                        "- Order ID: %d<br>" +
                        "- Course: %s<br>" +
                        "- Amount: %s %s<br>" +
                        "- Date: %s</p>" +
                        "<p>Your receipt is attached to this email as a PDF document. Please keep it for your records.</p>" +
                        "<p>If you have any questions about your order, please don't hesitate to contact our support team.</p>" +
                        "<p>Happy learning!</p>" +
                        "<p>Best regards,<br>" +
                        "The Learniverse Connect Team</p>" +
                        "</body></html>",
                userName,
                order.getId(),
                order.getCourse().getCourseName(),
                order.getPrice(),
                order.getCurrency(),
                order.getCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        );
    }
}