package no.ntnu.idata2306.mapper;

import no.ntnu.idata2306.dto.OrderStatusDto;
import no.ntnu.idata2306.model.payment.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderStatusMapper {
   OrderStatusMapper INSTANCE = Mappers.getMapper(OrderStatusMapper.class);

    @Named("orderStatusToOrderStatusDto")
    @Mapping(source = "status", target = "orderStatus")
    OrderStatusDto orderStatusToOrderStatusDto(OrderStatus orderStatus);
}
