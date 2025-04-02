package no.ntnu.idata2306.mapper;

import no.ntnu.idata2306.dto.ProviderRequestDto;
import no.ntnu.idata2306.dto.ProviderResponseDto;
import no.ntnu.idata2306.model.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between Provider entities and various Provider DTOs.
 * Utilizes MapStruct for automatic generation of mapping implementations.
 */
@Mapper
public interface ProviderMapper {

    /**
     * Instance of the ProviderMapper to be used for mapping operations.
     */
    ProviderMapper INSTANCE = Mappers.getMapper(ProviderMapper.class);

    /**
     * Converts a ProviderRequestDto to a Provider entity.
     *
     * @param providerRequestDto the DTO containing provider creation information
     * @return the Provider entity
     */
    Provider providerRequestDtoToProvider(ProviderRequestDto providerRequestDto);

    /**
     * Updates an existing Provider entity with the values from a ProviderRequestDto.
     *
     * @param providerRequestDto the DTO containing updated provider information
     * @param provider the Provider entity to be updated
     */
    void updateProviderFromDto(ProviderRequestDto providerRequestDto, @MappingTarget Provider provider);

    /**
     * Converts a Provider entity to a ProviderResponseDto.
     *
     * @param provider the Provider entity
     * @return the ProviderResponseDto
     */
    ProviderResponseDto providerToProviderResponseDto(Provider provider);
}
