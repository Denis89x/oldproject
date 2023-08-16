package com.example.project1.service;

import com.example.project1.model.Book;
import com.example.project1.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    void save(Person person);

    void delete(Integer id);

    Person getById(Integer id);

    List<Person> getAll();

    Optional<Person> getByName(String name);

    void update(Integer id, Person person);
}
