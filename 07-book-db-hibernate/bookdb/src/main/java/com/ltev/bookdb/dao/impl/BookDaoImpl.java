package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDaoImpl extends AbstractDaoImpl<Book> implements BookDao {

    public BookDaoImpl(EntityManagerFactory emf) {
        super(emf, new Book());
    }

    @Override
    public List<Book> findByTitle(String title) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("from Book where title = ?1")
                    .setParameter(1, title)
                    .getResultList();
        }
    }

    @Override
    public void deleteById(Long id) {
        var em = emf.createEntityManager();
        em.getTransaction().begin();
        var book = em.find(Book.class, id);
        book.setAuthor(null);
        em.remove(book);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<Book> findByIsbnLike(String isbnStart) {
        try (var em = emf.createEntityManager()) {
            return em.createNamedQuery("findAllByIsbnLike", Book.class)
                    .setParameter(1, isbnStart + "%")
                    .getResultList();
        }
    }
}
