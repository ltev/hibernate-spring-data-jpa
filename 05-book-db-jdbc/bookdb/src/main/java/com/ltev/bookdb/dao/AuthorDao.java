package com.ltev.bookdb.dao;

import com.ltev.bookdb.domain.Author;

import java.util.List;

public interface AuthorDao extends BaseDao<Author, Long> {

    List<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
