package ninja.pinhole.model;

import java.util.List;

public interface Dao<T> {
    List<T> findAll();
    void insert(T t);
}
