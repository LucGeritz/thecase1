package ninja.pinhole.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ninja.pinhole.console.Pickable;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = "User.findByAlias", query = "SELECT u FROM User u WHERE u.alias = :alias"),
        @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id")
})
public class User implements Pickable {
    @Id
    @GeneratedValue
    private Long id;

    // Note: unique only works if JPA creates db for you!
    @Column(unique=true)
    private String alias;
    private String email;

    @Convert(converter = PasswordConverter.class)
    private String password;
    private boolean blocked;
    private boolean isAdmin;

    @OneToMany(mappedBy = "user")
    private List<Product> products;

    @Override
    public String getIdAsString() {
        return this.id+"";
    }

    @Override
    public String getRowAsString() {
        return String.format("%-14s %1s %1s", this.alias , this.isAdmin ? "A" : " ", this.isBlocked() ? "B" : " ");
    }
}


