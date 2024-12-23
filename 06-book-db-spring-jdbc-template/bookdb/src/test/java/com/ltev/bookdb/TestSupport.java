package com.ltev.bookdb;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;

public class TestSupport {

    public static boolean equalsWithId(Author a1, Author a2) {
        return  a1.getId().equals(a2.getId())
                && equalsNoId(a1, a2);
    }

    public static boolean equalsNoId(Author a1, Author a2) {
        return  a1.getFirstName().equals(a2.getFirstName())
                && a1.getLastName().equals(a2.getLastName());
    }

    public static boolean equalsWithId(Book b1, Book b2) {
        return  b1.getId().equals(b2.getId())
                && equalsNoId(b1, b2);
    }

    public static boolean equalsNoId(Book b1, Book b2) {
        return  b1.getTitle().equals(b2.getTitle())
                && b1.getPublisher().equals(b2.getPublisher())
                && b1.getIsbn().equals(b2.getIsbn());
    }
}
