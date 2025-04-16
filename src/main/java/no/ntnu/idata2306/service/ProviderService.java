package no.ntnu.idata2306.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.ProviderRequestDto;
import no.ntnu.idata2306.dto.ProviderResponseDto;
import no.ntnu.idata2306.mapper.ProviderMapper;
import no.ntnu.idata2306.model.Provider;
import no.ntnu.idata2306.repository.ProviderRepository;
import no.ntnu.idata2306.util.repository.RepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ProviderService {

    private final ProviderRepository providerRepository;

    /**
     * Constructs a new instance of ProviderService.
     *
     * @param providerRepository the repository for managing provider data
     */
    @Autowired
    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    /**
     * Retrieves all providers from the repository and converts them to ProviderResponseDto objects.
     *
     * @return a list of ProviderResponseDto objects representing all providers.
     */
    public List<ProviderResponseDto> getAllProviders() {
        return this.providerRepository.findAll().stream()
                .map(ProviderMapper.INSTANCE::providerToProviderResponseDto)
                .toList();
    }

    /**
     * Retrieves a provider by its ID.
     *
     * @param id the ID of the provider to retrieve
     * @return the ProviderResponseDto object if found
     * @throws EntityNotFoundException if the provider with the specified ID is not found
     */
    public ProviderResponseDto getProviderById(int id) {
        Provider provider = findProviderById(id);
        return ProviderMapper.INSTANCE.providerToProviderResponseDto(provider);
    }

    /**
     * Creates a new provider with the provided information.
     *
     * @param providerRequestDto the DTO containing the provider information
     * @return the newly created ProviderResponseDto object
     */
    public ProviderResponseDto createProvider(ProviderRequestDto providerRequestDto) {
        Provider provider = ProviderMapper.INSTANCE.providerRequestDtoToProvider(providerRequestDto);
        provider.setCreated(LocalDateTime.now());

        this.providerRepository.save(provider);
        return ProviderMapper.INSTANCE.providerToProviderResponseDto(provider);
    }

    /**
     * Updates an existing provider with the provided information.
     *
     * @param id the ID of the provider to update
     * @param providerRequestDto the DTO containing updated provider information
     * @return the updated ProviderResponseDto object
     * @throws EntityNotFoundException if the provider with the specified ID is not found
     */
    public ProviderResponseDto updateProvider(int id, ProviderRequestDto providerRequestDto) {
        Provider provider = findProviderById(id);
        ProviderMapper.INSTANCE.updateProviderFromDto(providerRequestDto, provider);
        provider.setUpdated(LocalDateTime.now());

        this.providerRepository.save(provider);
        return ProviderMapper.INSTANCE.providerToProviderResponseDto(provider);
    }

    /**
     * Deletes a provider by its ID.
     *
     * @param id the ID of the provider to delete
     * @throws EntityNotFoundException if the provider with the specified ID is not found
     */
    public void deleteProvider(int id) {
        Provider provider = findProviderById(id);

        this.providerRepository.delete(provider);
        log.info("Provider deleted with ID: {}", id);
    }

    /**
     * Finds a provider by its ID.
     *
     * @param id the ID of the provider to be found
     * @return the Provider object if found
     * @throws EntityNotFoundException if the provider with the specified ID is not found
     */
    public Provider findProviderById(int id) {
        return RepositoryUtils.findEntityById(providerRepository::findById, id, Provider.class);
    }
}
