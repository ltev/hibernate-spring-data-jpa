package com.ltev.bookdb.dao.hibernate;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDaoHibernate extends AbstractDaoHibernate<Author> implements AuthorDao {

    public AuthorDaoHibernate(EntityManagerFactory emf) {
        super(emf, Author.class);
    }

    @Override
    public int saveInBatch(List<Author> authors) {
        runInsideTransaction((em) -> {authors.stream().forEach(em::persist);});
        return authors.size();
    }

    @Override
    public List<Book> findBooks(Long authorId) {
        try (var em = emf.createEntityManager()) {
            return em.createNativeQuery("select * from book where author_id = ?1")
                    .setParameter(1, authorId)
                    .getResultList();
        }
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

    @Override
    public List<Author> findByFirstNameAndLastName(String firstName, String lastName) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("from Author where firstName = ?1 and lastName = ?2", Author.class)
                    .setParameter(1, firstName)
                    .setParameter(2, lastName)
                    .getResultList();
        }
    }

    @Override
    public List<Author> findByLastNameSortByFirstName(String lastName) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("from Author where lastName = ?1 order by firstName")
                    .setParameter(1, lastName)
                    .getResultList();
        }
    }

    @Override
    public List<Author> findByLastNameSortByFirstName(String lastName, Pageable pageable) {
        try (var em = emf.createEntityManager()) {
            Sort.Order order = pageable.getSort().getOrderFor("first_name");
            String sql = new StringBuilder("from Author where lastName = ?1 order by firstName ")
                    .append(order != null ? order.getDirection() : "asc")
                    .append(" limit ?2 offset ?3")
                    .toString();
            return em.createQuery(sql, Author.class)
                    .setParameter(1, lastName)
                    .setParameter(2, pageable.getPageSize())
                    .setParameter(3, pageable.getOffset())
                    .getResultList();
        }
    }
}
