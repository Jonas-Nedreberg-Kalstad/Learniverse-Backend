package no.ntnu.idata2306.mapper.course.details;

import no.ntnu.idata2306.dto.course.details.CreditDto;
import no.ntnu.idata2306.model.course.details.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreditMapper {
    CreditMapper INSTANCE = Mappers.getMapper(CreditMapper.class);

    CreditDto creditToCreditDto(Credit credit);
}
