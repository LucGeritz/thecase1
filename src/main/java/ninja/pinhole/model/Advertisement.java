package ninja.pinhole.model;

import lombok.*;
import ninja.pinhole.console.Pickable;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = "Adv.findById", query = "SELECT a FROM Advertisement a WHERE a.id = :id"),
        @NamedQuery(name = "Adv.findByName", query = "SELECT a FROM Advertisement a WHERE a.name = :name"),
})
public class Advertisement implements Pickable {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @ManyToOne (fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;

    // Converter voor twee decimalen?
    private BigDecimal price;

    @Override
    public String getIdAsString() {
        return this.id+"";
    }

    @Override
    public String getRowAsString() {
        return this.name;
    }
}


