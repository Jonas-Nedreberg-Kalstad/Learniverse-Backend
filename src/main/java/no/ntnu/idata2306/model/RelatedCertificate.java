package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
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

    @JsonBackReference
    @ManyToMany(mappedBy = "relatedCertificates")
    @Schema(description = "Courses with the given relatedCertificate(s)")
    private Set<Course> courses = new LinkedHashSet<>();

    public RelatedCertificate() {
    }

    /**
     * Constructor
     * @param name name
     */
    public RelatedCertificate(String name) {
        this.certificateName = name;
    }
}
