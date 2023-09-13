package org.example;

import java.util.List;

public interface Dao <T> {
    T getById(int id);
    List<T> findSimilarObjects(T baseObject);
    List<T> getAll();
    boolean save(T t);
    boolean delete(int id);
    boolean update(T baseObject);
}
