package com.ltev.bookdb.dao.hibernate;

import com.ltev.bookdb.dao.BaseDao;
import com.ltev.bookdb.domain.LongIdEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
public abstract class AbstractDaoHibernate<T extends LongIdEntity> implements BaseDao<T, Long> {

    protected EntityManagerFactory emf;
    protected Class<T> clazz;

    @Override
    public long count() {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("select count(*) from " + clazz.getSimpleName(), Long.class).getSingleResult();
        }
    }

    @Override
    public T save(T t) {
        if (t.getId() != null) {
            return update(t);
        }

        runInsideTransaction((em) -> {em.persist(t);});
        return t;
    }

    public T update(T t) {
        return runInsideTransaction((em) -> {return em.merge(t);} );
    }

    @Override
    public Optional<T> findById(Long id) {
        try (var em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(clazz, id));
        }
    }

    @Override
    public void deleteById(Long id) {
        runInsideTransaction((em) -> {em.remove(em.find(clazz, id));});
    }

    // == PROTECTED HELPER METHODS ==

    protected void runInsideTransaction(Consumer<EntityManager> consumer) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            consumer.accept(em);
            em.getTransaction().commit();
        }
    }

    protected <S> S runInsideTransaction(Function<EntityManager, S> function) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            S toReturn = function.apply(em);
            em.getTransaction().commit();
            return toReturn;
        }
    }
}
