package ninja.pinhole.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Service extends Advertisement implements AdvertisementCategory {
    private ServiceCategory category;

    @Override
    public String getRowAsString(){
        return(super.getRowAsString().concat(" Service ").concat(category.toString()));
    }

    @Override
    public void setCategory(String catName) {
        category  = ServiceCategory.valueOf(catName);
    }

    @Override
    public Iterable getEnumItems() {
        return Arrays.asList(ServiceCategory.values());
    }

    @Override
    public String getCategory(){
        return category.toString();
    }

}
