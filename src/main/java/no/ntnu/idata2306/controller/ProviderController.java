package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.ProviderRequestDto;
import no.ntnu.idata2306.dto.ProviderResponseDto;
import no.ntnu.idata2306.mapper.ProviderMapper;
import no.ntnu.idata2306.model.Provider;
import no.ntnu.idata2306.service.ProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@CrossOrigin()
@RequestMapping("/api")
@Tag(name = "Provider API", description = "Endpoints for providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    /**
     * Retrieves all providers.
     * Error code 500 is handled by global exception handler.
     *
     * @return a list of ProviderResponseDto objects representing all providers.
     */
    @Operation(summary = "Get all providers", description = "Retrieves a list of all providers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Providers retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProviderResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/providers")
    public List<ProviderResponseDto> getAllProviders() {
        return providerService.getAllProviders();
    }

    /**
     * Retrieves a provider by its ID.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the provider to retrieve
     * @return ResponseEntity with the ProviderResponseDto object and HTTP status
     */
    @Operation(summary = "Get provider by ID", description = "Retrieves a provider by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProviderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Provider not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/admin/providers/{id}")
    public ResponseEntity<ProviderResponseDto> getProvider(@PathVariable int id) {
        ProviderResponseDto provider = this.providerService.getProviderById(id);
        log.info("Provider found with ID: {}", id);
        return new ResponseEntity<>(provider, HttpStatus.OK);
    }

    /**
     * Creates a new provider.
     * Error code 400 and 500 is handled by global exception handler.
     *
     * @param providerRequestDto the DTO containing provider information
     * @return ResponseEntity with the created ProviderResponseDto object and HTTP status
     */
    @Operation(summary = "Create a new provider", description = "Creates a new provider with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Provider created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProviderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/admin/providers")
    public ResponseEntity<ProviderResponseDto> createProvider(@Valid @RequestBody ProviderRequestDto providerRequestDto) {
        ProviderResponseDto createdProvider = this.providerService.createProvider(providerRequestDto);
        log.info("Provider created with ID: {}", createdProvider.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProvider);
    }

    /**
     * Updates an existing provider.
     * Error code 400, 404, and 500 is handled by global exception handler.
     *
     * @param id the ID of the provider to update
     * @param providerRequestDto the DTO containing updated provider information
     * @return ResponseEntity with the updated ProviderResponseDto object and HTTP status
     */
    @Operation(summary = "Update a provider", description = "Updates an existing provider with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProviderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Provider not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/admin/providers/{id}")
    public ResponseEntity<ProviderResponseDto> updateProvider(@PathVariable int id, @Valid @RequestBody ProviderRequestDto providerRequestDto) {
        ProviderResponseDto updatedProvider = this.providerService.updateProvider(id, providerRequestDto);
        log.info("Provider updated with ID: {}", id);
        return ResponseEntity.ok(updatedProvider);
    }

    /**
     * Deletes a provider by its ID.
     * Error code 404 and 500 is handled by global exception handler.
     *
     * @param id the ID of the provider to delete
     * @return ResponseEntity with HTTP status
     */
    @Operation(summary = "Delete a provider", description = "Deletes a provider by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Provider deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Provider not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/admin/providers/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable int id) {
        this.providerService.deleteProvider(id);
        log.info("Provider deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
