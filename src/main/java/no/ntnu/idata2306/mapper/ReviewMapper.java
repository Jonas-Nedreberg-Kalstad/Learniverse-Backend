package no.ntnu.idata2306.mapper;

import no.ntnu.idata2306.dto.review.ReviewRequestDto;
import no.ntnu.idata2306.dto.review.ReviewResponseDto;
import no.ntnu.idata2306.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between Review entities and various Review DTOs.
 * Utilizes MapStruct for automatic generation of mapping implementations.
 */
@Mapper
public interface ReviewMapper {

    /**
     * Instance of the ReviewMapper to be used for mapping operations.
     */
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    /**
     * Converts a ReviewRequestDto to a Review entity.
     *
     * @param reviewRequestDto the DTO containing review creation information
     * @return the Review entity
     */
    Review reviewRequestDtoToReview(ReviewRequestDto reviewRequestDto);


    /**
     * Converts a Review entity to a ReviewResponseDto.
     *
     * @param review the Review entity
     * @return the ReviewResponseDto
     */
    ReviewResponseDto reviewToReviewResponseDto(Review review);
}
