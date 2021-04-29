package ninja.pinhole.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;



class PasswordConverterTest {
    @Test
    void WhenPasswordIsStoredInDBThenItIsHashed(){
        String pw = "geheim";
        var pwc = new PasswordConverter();

        String hash = pwc.convertToDatabaseColumn(pw);
        Assertions.assertThat(hash).isNotEqualTo(pw);
        pw = pwc.convertToEntityAttribute(hash);
        Assertions.assertThat(hash).isNotEqualTo(pw);
    }

}