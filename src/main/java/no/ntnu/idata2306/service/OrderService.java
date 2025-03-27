package no.ntnu.idata2306.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.order.OrderPaymentDto;
import no.ntnu.idata2306.dto.order.OrderResponseDto;
import no.ntnu.idata2306.enums.OrderStatusEnum;
import no.ntnu.idata2306.enums.PaymentMethodEnum;
import no.ntnu.idata2306.mapper.OrderPaymentMapper;
import no.ntnu.idata2306.mapper.course.CourseEnrollmentsMapper;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.model.course.Course;
import no.ntnu.idata2306.model.course.CourseEnrollments;
import no.ntnu.idata2306.model.payment.*;
import no.ntnu.idata2306.repository.course.CourseEnrollmentsRepository;
import no.ntnu.idata2306.repository.payment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final CourseEnrollmentsRepository courseEnrollmentsRepository;
    private final CourseService courseService;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentCardRepository paymentCardRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository, PaymentRepository paymentRepository, OrderStatusRepository orderStatusRepository,
                        CourseEnrollmentsRepository courseEnrollmentsRepository, CourseService courseService,
                        PaymentMethodRepository paymentMethodRepository, PaymentCardRepository paymentCardRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.courseEnrollmentsRepository = courseEnrollmentsRepository;
        this.courseService = courseService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentCardRepository = paymentCardRepository;
    }

    /**
     * Processes an order payment using a debit card and enrolls the user in the course.
     * This method creates a new order, adds payment card information, processes the payment, and updates the order status.
     * If the payment is successful, the user is enrolled in the course. If any step fails, the transaction is rolled back.
     *
     * @param orderPaymentDto the DTO containing order payment information
     * @param user the user who is placing the order
     * @return the OrderResponseDto containing detailed order information
     * @throws EntityNotFoundException if the course is not found with the given ID
     */
    @Transactional
    public OrderResponseDto processOrderPaymentWithDebitCard(OrderPaymentDto orderPaymentDto, User user) {
        LocalDateTime created = LocalDateTime.now();

        Course course = courseService.getCourseById(orderPaymentDto.getCourseId());

        // Create the order
        Orders order = OrderPaymentMapper.INSTANCE.toOrder(orderPaymentDto);
        order.setOrderStatus(orderStatusRepository.findByStatus(OrderStatusEnum.PENDING_PAYMENT));
        order.setCreated(created);
        order.setUser(user);
        order.setCourse(course);
        order.setCurrency(course.getCurrency().getCurrency());
        Orders savedOrder = orderRepository.save(order);
        log.info("Order created with ID: {}", savedOrder.getId());

        // Adding card information
        PaymentCard paymentCard = OrderPaymentMapper.INSTANCE.toPaymentCard(orderPaymentDto);
        paymentCard.setUser(user);
        paymentCard.setCreated(created);
        paymentCardRepository.save(paymentCard);

        // Process the payment
        Payment payment = OrderPaymentMapper.INSTANCE.toPayment(orderPaymentDto);
        payment.setCreated(created);
        PaymentMethod paymentMethod = paymentMethodRepository.findByMethod(PaymentMethodEnum.DEBIT_CARD);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentCard(paymentCard);
        paymentRepository.save(payment);

        // Check if payment is successful
        if (paymentSuccessful(payment)) {

            // Update the order with payment details after payment success
            savedOrder.setPayment(payment);
            savedOrder.setUpdated(created);
            savedOrder.setOrderStatus(orderStatusRepository.findByStatus(OrderStatusEnum.PAYMENT_COMPLETED));
            savedOrder = orderRepository.save(savedOrder);
            log.info("Payment successful for Order ID: {}", savedOrder.getId());

            // Enroll user in course. Setting enrollment to true due to payment is a success
            CourseEnrollments courseEnrollments = CourseEnrollmentsMapper.INSTANCE.toCourseEnrollments(course, user, created, true);
            courseEnrollmentsRepository.save(courseEnrollments);
            log.info("User enrolled in course with Order ID: {}", savedOrder.getId());
            OrderResponseDto orderResponseDto = OrderPaymentMapper.INSTANCE.ordersToOrderResponseDto(savedOrder);
            log.error("User enrolled in course with Order: {}", orderResponseDto);
        } else {
            savedOrder.setOrderStatus(orderStatusRepository.findByStatus(OrderStatusEnum.PAYMENT_FAILED));
            orderRepository.save(savedOrder);
            log.warn("Payment failed for Order ID: {}", savedOrder.getId());
        }

        return OrderPaymentMapper.INSTANCE.ordersToOrderResponseDto(savedOrder);
    }

    /**
     * Validates the payment by integrating with an external payment provider API.
     * This method should be implemented to call the actual payment provider's API
     * to verify if the payment was successful.
     *
     * The implementation generally involves preparing the request, calling the payment provider API,
     * handling the response, and returning the result.
     *
     * Example implementation outline:
     *
     * <pre>
     * {@code
     * private boolean paymentSuccessful(Payment payment) {
     *     // Step 1: Prepare the request
     *     PaymentRequest paymentRequest = new PaymentRequest();
     *     paymentRequest.setAmount(payment.getAmount());
     *     paymentRequest.setPaymentMethod(payment.getPaymentMethod());
     *     paymentRequest.setCardToken(payment.getPaymentCard().getCardToken());
     *
     *     // Step 2: Call the payment provider API
     *     RestTemplate restTemplate = new RestTemplate();
     *     ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(
     *         "https://api.paymentprovider.com/validate", paymentRequest, PaymentResponse.class);
     *
     *     // Step 3: Handle the response
     *     if (response.getStatusCode() == HttpStatus.OK && response.getBody().isSuccessful()) {
     *         // Step 4: Return the result
     *         return true;
     *     } else {
     *         return false;
     *     }
     * }
     * }
     * </pre>
     *
     * Note: The actual implementation will vary based on the specific payment provider's API and requirements.
     * Ensure to handle exceptions and edge cases appropriately.
     *
     * @param payment the Payment entity containing payment details
     * @return true if the payment was successful, otherwise false
     */
    private boolean paymentSuccessful(Payment payment) {
        return true; // Simulating payment successful
    }
}
