package ninja.pinhole.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ninja.pinhole.console.Pickable;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = "Adv.findById", query = "SELECT a FROM Advertisement a WHERE a.id = :id"),
        @NamedQuery(name = "Adv.delById", query = "DELETE FROM Advertisement a WHERE a.id = :id"),
        @NamedQuery(name = "Adv.findByName", query = "SELECT a FROM Advertisement a WHERE a.name = :name"),
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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
        return String.format("%-20s",this.name);
    }

}


