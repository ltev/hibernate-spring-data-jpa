package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDaoImpl extends AbstractDaoImpl<Author> implements AuthorDao {

    public AuthorDaoImpl(EntityManagerFactory emf) {
        super(emf, new Author());
    }

    @Override
    public List<Author> findByFirstNameAndLastName(String firstName, String lastName) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("from Author where firstName = ?1 and lastName = ?2")
                    .setParameter(1, firstName)
                    .setParameter(2, lastName)
                    .getResultList();
        }
    }

    @Override
    public int saveInBatch(List<Author> authors) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        for (Author author : authors) {
            em.persist(author);
        }
        em.flush();
        em.getTransaction().commit();
        em.close();
        return authors.size();
    }

    @Override
    public List<Book> findBooks(Long authorId) {
        return null;
    }

    @Override
    public Optional<Author> findByIdJoinFetchBooks(Long authorId) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("from Author a left join fetch a.books where a.id = :id", Author.class)
                    .setParameter("id", authorId)
                    .getResultStream()
                    .findFirst();
        }
    }
}
