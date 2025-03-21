package no.ntnu.idata2306.mapper.course.details;

import no.ntnu.idata2306.dto.course.details.CurrencyDto;
import no.ntnu.idata2306.model.course.details.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyDto currencyToCurrencyDto(Currency currency);
}
