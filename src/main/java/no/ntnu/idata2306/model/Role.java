package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Schema(description = "Roles which users may have.", name = "role")
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "role id")
    private int id;

    @Column(name = "role", nullable = false, unique = true)
    @Schema(description = "role name")
    private String role;

    @JsonBackReference
    @ManyToMany(mappedBy = "roles")
    @Schema(description = "Users with the given role(s)")
    private Set<User> users = new LinkedHashSet<>();
}