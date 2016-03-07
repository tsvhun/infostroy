package dao;

import java.util.List;

/**
 * Created by Alexander on 16.02.2016.
 */
public interface CRUDinterface<T> {
    T create(T obj);
    T get(int id);
    void update(T obj);
    void delete(int id);
    List<T> getAll();
}
