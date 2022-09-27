package sit.int221.integratedprojectbe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_roles")
public class UserRole {
    @Id
    private String name;

    @Column
    private String label;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Set<User> roles = new LinkedHashSet<>();
}
