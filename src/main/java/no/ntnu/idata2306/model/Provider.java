package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import no.ntnu.idata2306.model.course.Course;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Provider.class)
@Schema(description = "Provider details", name = "provider")
@Entity
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "Provider ID")
    private int id;

    @Column(name = "provider_name", nullable = false)
    @Schema(description = "Provider name")
    private String providerName;

    @Column(name = "provider_url", nullable = true)
    @Schema(description = "Provider URL")
    private String providerUrl;

    @Column(name = "provider_logo_url", nullable = true)
    @Schema(description = "Provider logo URL")
    private String providerLogoUrl;

    @Column(name = "created", nullable = false)
    @Schema(description = "Creation date of the provider")
    private LocalDateTime created;

    @Column(name = "updated", nullable = true)
    @Schema(description = "Update date of the provider")
    private LocalDateTime updated;

    @OneToMany(mappedBy = "provider")
    @Schema(description = "agreements associated with the provider")
    private Set<Agreement> agreements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "users associated with the provider")
    private Set<User> users = new LinkedHashSet<>();

    @OneToMany(mappedBy = "provider")
    @Schema(description = "courses associated with the provider")
    private Set<Course> courses = new LinkedHashSet<>();
}