package no.ntnu.idata2306.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderResponseDto {

    private int id;
    private String providerName;
    private String providerUrl;
    private String providerLogoUrl;
    private LocalDateTime created;
    private LocalDateTime updated;
}
