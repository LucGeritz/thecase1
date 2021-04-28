package ninja.pinhole.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Service extends Advertisement implements AdvertisementCategory {
    private ServiceCategory category;

    @Override
    public String getRowAsString(){
        return(super.getRowAsString().concat(" Service ").concat(category.value));
    }

    @Override
    public String getCatName() {
        return category.value;
    }

}
