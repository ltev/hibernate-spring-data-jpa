package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.BaseDao;
import com.ltev.bookdb.domain.LongIdEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public abstract class AbstractDaoImpl<T extends LongIdEntity> implements BaseDao<T, Long> {

    private JpaRepository<T, Long> repository;

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public T save(T t) {
        return repository.save(t);
    }

    @Override
    public int saveInBatch(List<T> entities) {
        return repository.saveAll(entities).size();
    }

    @Override
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
