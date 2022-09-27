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
@Table(name = "event_category")
public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer categoryId;

    @Column(nullable = false, length = 100, unique = true)
    private String categoryName;

    @Column(length = 500)
    private String eventCategoryDesc;

    @Column(nullable = false)
    private Integer eventDuration;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private Set<Event> events = new LinkedHashSet<>();



}