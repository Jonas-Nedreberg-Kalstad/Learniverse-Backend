package no.ntnu.idata2306.mapper.course.details;

import no.ntnu.idata2306.dto.course.details.HoursPerWeekDto;
import no.ntnu.idata2306.model.course.details.HoursPerWeek;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HoursPerWeekMapper {
    HoursPerWeekMapper INSTANCE = Mappers.getMapper(HoursPerWeekMapper.class);

    HoursPerWeekDto hoursPerWeekToHoursPerWeekDto(HoursPerWeek hoursPerWeek);
}
