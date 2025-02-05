package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Agreement details", name = "agreement")
@Entity
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Agreement ID")
    private int id;

    @Column(name = "ad_fee", nullable = false)
    @Schema(description = "Advertisement fee")
    private BigDecimal adFee;

    @Column(name = "discount", nullable = false)
    @Schema(description = "Discount")
    private BigDecimal discount;

    @Column(name = "discounts_per_year", nullable = true)
    @Schema(description = "Discounts per year")
    private int discountsPerYear;

    @Column(name = "provider_name", nullable = false)
    @Schema(description = "Provider name")
    private String providerName;

    @Column(name = "created", nullable = false)
    @Schema(description = "Creation date of the agreement")
    private LocalDateTime created;

    @Column(name = "updated", nullable = true)
    @Schema(description = "Update date of the agreement")
    private LocalDateTime updated;

    @Lob
    @Column(name = "agreement_details", nullable = true)
    @Schema(description = "Details of the agreement")
    private String agreementDetails;

    @Lob
    @Column(name = "termination_clause", nullable = true)
    @Schema(description = "Termination clause of the agreement")
    private String terminationClause;

    @ManyToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @Schema(description = "Provider associated with the agreement")
    private Provider provider;

    @OneToMany(mappedBy = "agreement")
    @JsonBackReference
    @Schema(description = "advertisements associated with the agreement")
    private Set<Advertisement> advertisements = new LinkedHashSet<>();
}