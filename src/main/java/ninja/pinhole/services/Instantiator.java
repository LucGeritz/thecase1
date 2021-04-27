package ninja.pinhole.services;

@FunctionalInterface
public interface Instantiator<T> {
   T create();
}
