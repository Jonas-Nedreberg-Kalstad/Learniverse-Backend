package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Schema(description = "Roles which users may have.", name = "role")
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "role id")
    private int id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "name", nullable = false, unique = true)
    @Schema(description = "role name")
    private String roleName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonBackReference
    @ManyToMany(mappedBy = "roles")
    @Schema(description = "Users with the given role(s)")
    private Set<User> users = new LinkedHashSet<>();

    public Role() {
    }

    /**
     * Sets the value of the name field to given value.
     * @param name name
     */
    public Role(String name) {
        this.roleName = name;
    }
}
