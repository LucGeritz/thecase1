package ninja.pinhole.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import java.util.Arrays;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product extends Advertisement implements AdvertisementCategory {
    private ProductCategory category;

    @Override
    public String getRowAsString(){
        return(super.getRowAsString().concat(" Product ").concat(category.toString()));
    }

    @Override
    public void setCategory(String catName) {
        category  = ProductCategory.valueOf(catName);
    }

    @Override
    public Iterable getEnumItems() {
        return Arrays.asList(ProductCategory.values());
    }

    @Override
    public String getCategory(){
        return category.toString();
    }
}
