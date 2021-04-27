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
        @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
        @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name = :name"),
})
public class Product implements Pickable {
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


