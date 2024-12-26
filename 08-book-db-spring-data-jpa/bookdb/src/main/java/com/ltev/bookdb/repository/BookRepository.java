package com.ltev.bookdb.repository;

import com.ltev.bookdb.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitle(String title);

    @Query("from Book where title = ?1")
    List<Book> findByTitleQuery(String title);

    @Query("from Book where title = :title")
    List<Book> findByTitleQueryNamed(@Param("title") String title);

    // @Query(nativeQuery = true, value = "select * from book where title = :title")
    @NativeQuery("select * from book where title = :title")
    List<Book> findByTitleNativeQuery(@Param("title") String title);

    @Nullable
    Book findByIsbn(@Nullable String isbn);

    @Async
    Future<Book> queryByIsbn(String isbn);

    List<Book> jpaNamedQueryForFindingAllByIsbnLike(String isbnStart);
}
