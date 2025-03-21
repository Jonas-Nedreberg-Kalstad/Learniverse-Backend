package no.ntnu.idata2306.mapper.course.details;

import no.ntnu.idata2306.dto.course.details.CategoryDto;
import no.ntnu.idata2306.model.course.details.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto categoryToCategoryDto(Category category);
}
