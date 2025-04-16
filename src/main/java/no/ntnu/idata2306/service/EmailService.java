package no.ntnu.idata2306.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.order.OrderResponseDto;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.model.payment.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending emails.
 * This service provides functionality for sending various types of emails, including
 * receipts with PDF attachments.
 */
@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserService userService;
    
    @Value("${spring.mail.username}")
    private String senderEmail;
    
    @Value("${application.name:Course Platform}")
    private String applicationName;

    /**
     * Constructs a new instance of EmailService.
     *
     * @param mailSender the mail sender for sending emails
     * @param userService the service for user operations
     */
    @Autowired
    public EmailService(JavaMailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    /**
     * Sends a receipt email with a PDF attachment.
     *
     * @param recipientEmail the email address to send the receipt to
     * @param subject the subject line for the email
     * @param bodyText the body text of the email
     * @param pdfContent the PDF content to attach
     * @param fileName the filename for the attachment
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendReceiptEmail(String recipientEmail, String subject, String bodyText, 
                                   byte[] pdfContent, String fileName) {
        try {
            // Prepare the email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(bodyText, true);
            
            // Attach the PDF receipt
            helper.addAttachment(fileName, new ByteArrayResource(pdfContent));
            
            // Send the email
            mailSender.send(message);
            log.info("Email sent successfully to {}", recipientEmail);
            return true;
        } catch (MessagingException e) {
            log.error("Error creating email message", e);
            return false;
        } catch (MailException e) {
            log.error("Error sending email", e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error while sending email", e);
            return false;
        }
    }
    
    /**
     * Sends a receipt email for a specific order.
     *
     * @param orderId the ID of the order
     * @param userId the ID of the user (can be null for admin operations)
     * @param overrideEmail optional email address to override the user's email
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendOrderReceiptEmail(int orderId, Integer userId, String overrideEmail) {
        try {
            // Get services through application context to avoid circular dependency
            ReceiptService receiptService = ApplicationContextProvider.getBean(ReceiptService.class);
            OrderService orderService = ApplicationContextProvider.getBean(OrderService.class);
            
            // Generate the receipt PDF
            byte[] pdfContent = receiptService.generateReceiptPdf(orderId);

            Orders order = orderService.findOrderById(orderId);
            
            User user;
            String recipientEmail;
            if (overrideEmail != null && !overrideEmail.isEmpty()) {
                user = order.getUser();
                recipientEmail = overrideEmail;
            } else {
                user = userService.findUserById(userId);
                recipientEmail = user.getEmail();
            }
            
            // Create email content
            String subject = applicationName + "Learniverse Receipt " + orderId;
            String emailBody = receiptService.createReceiptEmailBody(order, user);
            
            // Send the email with the receipt
            return sendReceiptEmail(
                recipientEmail,
                subject,
                emailBody,
                pdfContent,
                "receipt-order-" + orderId + ".pdf"
            );
        } catch (Exception e) {
            log.error("Failed to send order receipt email for order ID: {}", orderId, e);
            return false;
        }
    }
}