package com.ltev.bookdb.dao;

import java.util.Optional;

public interface BaseDao<T, ID> {

    long count();

    T save(T t);

    Optional<T> findById(ID id);

    void deleteById(ID id);
}
