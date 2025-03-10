package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = RelatedCertificate.class)
@JsonIgnoreProperties("courses")
@Schema(description = "Related certificate which courses may have.", name = "related_certificate")
@Entity
public class RelatedCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "related certificate id")
    private int id;

    @Column(name = "related_certificate", nullable = false, unique = true)
    @Schema(description = "related certificate certificateName")
    private String certificateName;

    @ManyToMany(mappedBy = "relatedCertificates")
    @Schema(description = "Courses with the given relatedCertificate(s)")
    private Set<Course> courses = new LinkedHashSet<>();
}
