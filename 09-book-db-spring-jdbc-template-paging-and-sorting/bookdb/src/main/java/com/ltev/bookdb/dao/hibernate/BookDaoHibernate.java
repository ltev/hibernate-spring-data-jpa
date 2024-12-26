package com.ltev.bookdb.dao.hibernate;

import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class BookDaoHibernate extends AbstractDaoHibernate<Book> implements BookDao {

    public BookDaoHibernate(EntityManagerFactory emf) {
        super(emf, Book.class);
    }

    @Override
    public List<Book> findByTitle(String title) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("from Book where title = ?1", Book.class)
                    .setParameter(1, title)
                    .getResultList();
        }
    }

    @Override
    public List<Book> findAll() {
        return runQueryGetList("from Book", Book.class, List.of());
    }

    @Override
    public List<Book> findAll(int pageSize, int offset) {
        // can not put 'limit' directly after Book
        return runQueryGetList("from Book order by id limit ?1 offset ?2", Book.class, List.of(pageSize, offset));
    }

    @Override
    public List<Book> findAll(Pageable pageable, Sort sort) {
        StringBuilder hqlBuilder = new StringBuilder("from Book");
        if (sort.isSorted()) {
            hqlBuilder.append(" order by ");
            sort.get().forEach(o -> hqlBuilder
                    .append(o.getProperty())
                    .append(" ")
                    .append(o.getDirection())
                    .append(","));
            hqlBuilder.deleteCharAt(hqlBuilder.length() - 1);
        }
        return runQueryGetList(hqlBuilder.toString(), Book.class, List.of(), pageable);
    }

    @Override
    public List<Book> findAllByTitle(String title, int pageSize, int offset) {
        return runQueryGetList("from Book where title = ?3 order by id limit ?1 offset ?2", Book.class, List.of(pageSize, offset, title));
    }

    @Override
    public List<Book> findAllByTitle(String title, Pageable pageable) {
        return runQueryGetList("from Book where title = ?1", clazz, List.of(title), pageable);
    }

    @Override
    public List<Book> findAllSortByTitleAsc() {
        return runQueryGetList("from Book order by title asc", Book.class, List.of());
    }

    // == PRIVATE HELPER METHODS ==

    private <S> List<S> runQueryGetList(String sql, Class<S> clazz, List<Object> params) {
        try (var em = emf.createEntityManager()) {
            TypedQuery<S> query = em.createQuery(sql, clazz);
            int i = 1;
            for (Object param : params) {
                query.setParameter(i++, param);
            }
            return query.getResultList();
        }
    }

    private <S> List<S> runQueryGetList(String sql, Class<S> clazz, List<Object> params, Pageable pageable) {
        try (var em = emf.createEntityManager()) {
            TypedQuery<S> query = em.createQuery(sql, clazz)
                    .setMaxResults(pageable.getPageSize())
                    .setFirstResult(Math.toIntExact(pageable.getOffset()));
            int i = 1;
            for (Object param : params) {
                query.setParameter(i++, param);
            }
            return query.getResultList();
        }
    }
}
