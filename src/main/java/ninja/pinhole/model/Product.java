package ninja.pinhole.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product extends Advertisement{
    private ProductCategory category;

    @Override
    public String getRowAsString(){
        return(super.getRowAsString().concat(" Product ").concat(category.value));
    }
}
