package com.example.project1.service;

import com.example.project1.model.Book;
import com.example.project1.model.Person;

import java.util.List;
import java.util.Optional;

public interface BookService {

    void save(Book book);

    void delete(Integer id);

    Book getById(Integer id);

    List<Book> getAll();

    void update(Integer id, Book book);

    Optional<Book> getByName(String name);

    List<Book> getBooksByPersonId(Integer personId);

    void assign(Integer id, Person selectedPerson);

    Optional<Person> getBookOwner(Integer id);

    void setBookReleaseStatus(Integer bookId, boolean isReleased);

    Boolean isBookNameExists(String name);
}
