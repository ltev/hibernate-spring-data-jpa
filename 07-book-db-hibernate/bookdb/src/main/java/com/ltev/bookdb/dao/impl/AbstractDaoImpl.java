package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.BaseDao;
import com.ltev.bookdb.domain.LongIdEntity;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public abstract class AbstractDaoImpl<T extends LongIdEntity> implements BaseDao<T, Long> {

    protected final EntityManagerFactory emf;
    private T classObject;

    @Override
    public long count() {
        var em = emf.createEntityManager();
        long count = (long) em.createQuery("from " + classObject.getClass().getSimpleName() + " select count(*)").getSingleResult();
        em.close();
        return count;
    }

    @Override
    public T save(T t) {
        if (t.getId() != null) {
            return update(t);
        }

        var em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(t);
        em.getTransaction().commit();                           // Commits changes into the db
        em.close();
        return t;
    }

    public T update(T t) {
        var em = emf.createEntityManager();
        em.getTransaction().begin();
        t = em.merge(t);
        em.flush();         // force changes to db
        em.clear();         // clear cache
        em.getTransaction().commit();
        em.close();
        return t;
    }

    @Override
    public Optional<T> findById(Long id) {
        var em = emf.createEntityManager();
        var found = (T) em.find(classObject.getClass(), id);
        em.close();
        return Optional.ofNullable(found);
    }

    @Override
    public void deleteById(Long id) {
        var em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.find(classObject.getClass(), id));
        em.getTransaction().commit();
        em.close();
    }
}
