package no.ntnu.idata2306.mapper;

import no.ntnu.idata2306.dto.order.OrderPaymentDto;
import no.ntnu.idata2306.dto.order.OrderResponseDto;
import no.ntnu.idata2306.exception.CardTokenHashException;
import no.ntnu.idata2306.mapper.course.CourseMapper;
import no.ntnu.idata2306.model.payment.Orders;
import no.ntnu.idata2306.model.payment.Payment;
import no.ntnu.idata2306.model.payment.PaymentCard;
import no.ntnu.idata2306.util.HashUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for converting between OrderPaymentDto and entity classes.
 */
@Mapper(uses = {CourseMapper.class, OrderStatusMapper.class})
public interface OrderPaymentMapper {

    OrderPaymentMapper INSTANCE = Mappers.getMapper(OrderPaymentMapper.class);

    /**
     * Converts an OrderPaymentDto to an Orders entity.
     *
     * @param orderPaymentDto the DTO containing order payment information
     * @return the Orders entity
     */
    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "price", target = "price")
    Orders toOrder(OrderPaymentDto orderPaymentDto);

    /**
     * Converts an OrderPaymentDto to a Payment entity.
     *
     * @param orderPaymentDto the DTO containing payment information
     * @return the Payment entity
     */
    @Mapping(source = "price", target = "amount")
    Payment toPayment(OrderPaymentDto orderPaymentDto);

    /**
     * Converts an OrderPaymentDto to a PaymentCard entity.
     * The cardToken is hashed using SHA-512 for security.
     *
     * @param orderPaymentDto the DTO containing payment card information
     * @return the PaymentCard entity
     */
    @Mapping(source = "cardToken", target = "cardToken", qualifiedByName = "hashCardToken")
    @Mapping(source = "lastFourDigits", target = "lastFourDigits")
    @Mapping(source = "cardExpirationDate", target = "expirationDate")
    PaymentCard toPaymentCard(OrderPaymentDto orderPaymentDto);

    /**
     * Converts an Orders entity to an OrderResponseDto.
     *
     * @param orders the Orders entity
     * @return the OrderResponseDto
     */
    @Mapping(source = "course", target = "course", qualifiedByName = "courseToResponseCourseDto")
    @Mapping(source = "orderStatus", target = "orderStatus", qualifiedByName = "orderStatusToOrderStatusDto")
    OrderResponseDto ordersToOrderResponseDto(Orders orders);

    /**
     * Custom mapping method to hash the card token.
     * This method uses the HashUtils class to generate a hashed value for the card token.
     * If an error occurs during hashing, a CardTokenHashException is thrown.
     *
     * @param cardToken the card token to be hashed
     * @return the hashed card token
     * @throws CardTokenHashException if there is an error generating the hash
     */
    @Named("hashCardToken")
    default String hashCardToken(String cardToken) throws CardTokenHashException {
        return HashUtils.generateCardTokenHash(cardToken);
    }
}
