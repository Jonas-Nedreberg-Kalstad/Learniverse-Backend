package no.ntnu.idata2306.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO to return the Levenshtein calculated distance and detected transpositions
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LevenshteinDto {
    int distance;
    int transpositions;
}
