package ninja.pinhole.console;

import java.util.function.Predicate;

@FunctionalInterface
public interface RecordFilter<T> {
   boolean filter(T t);
}
