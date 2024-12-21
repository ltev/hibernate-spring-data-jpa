package com.ltev.bookdb.dao;

import java.util.Optional;

public interface BaseDao<T, ID> {

    T save(T t);

    Optional<T> findById(ID id);
}
