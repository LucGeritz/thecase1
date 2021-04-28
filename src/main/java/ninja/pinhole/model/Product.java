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
        return(super.getRowAsString().concat(" Product ").concat(category.value));
    }


    @Override
    public String getCatName() {
        return category.value;
    }




}
