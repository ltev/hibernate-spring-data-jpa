package com.ltev.bookdb.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T, ID> {

    long count();

    T save(T t);

    int saveInBatch(List<T> entities);

    Optional<T> findById(ID id);

    void deleteById(ID id);
}
