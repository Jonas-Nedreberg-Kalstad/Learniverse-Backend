package no.ntnu.idata2306.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderRequestDto {
    private String providerName;
    private String providerUrl;
    private String providerLogoUrl;
}
