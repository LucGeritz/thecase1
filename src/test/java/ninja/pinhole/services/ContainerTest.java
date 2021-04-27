package ninja.pinhole.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ContainerTest {

    @Test
    void whenObjectIsStoredInRegistryThenItCanBeRetrieved() {
        var reg = new Container();
        reg.set("some_fine_object", () -> new SomeFineClass("Bread"));
        var sfo = reg.<SomeFineClass>get("some_fine_object");
        Assertions.assertThat(sfo).isInstanceOf(SomeFineClass.class);
        Assertions.assertThat(sfo.someWord).isEqualTo("Bread");
    }

    @Test
    void whenKeyDoesNotExistInRegistryThenNullIsReturned() {
        var reg = new Container();
        var sfo= reg.<SomeFineClass>get("not_there");
        Assertions.assertThat(sfo).isNull();
    }

}

class SomeFineClass{
    String someWord;
    SomeFineClass(String someWord){
        this.someWord = someWord;
    }
}