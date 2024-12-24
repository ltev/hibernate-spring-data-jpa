package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.BaseDao;
import com.ltev.bookdb.domain.LongIdEntity;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public abstract class AbstractDaoImpl<T extends LongIdEntity> implements BaseDao<T, Long> {

    @Override
    public long count() {
        return 0;
    }

    @Override
    public T save(T t) {
        return null;
    }

    @Override
    public Optional<T> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long aLong) {

    }
}
