package ninja.pinhole.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.security.Provider;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Service extends Advertisement{
    private ServiceCategory category;

    @Override
    public String getRowAsString(){
        return(super.getRowAsString().concat(" Service ").concat(category.value));
    }
}
