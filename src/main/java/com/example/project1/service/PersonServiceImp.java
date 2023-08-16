package com.example.project1.service;

import com.example.project1.model.Book;
import com.example.project1.model.Person;
import com.example.project1.repository.BookRepository;
import com.example.project1.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImp implements PersonService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;

    @Autowired
    public PersonServiceImp(PersonRepository personRepository, BookRepository bookRepository) {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void save(Person person) {
        personRepository.save(person);
    }

    @Override
    public void delete(Integer id) {
        personRepository.deleteById(id);
    }

    @Override
    public Person getById(Integer id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isEmpty()) {
            throw new RuntimeException("Person not found");
        }
        return personOptional.get();
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> getByName(String name) {
        return personRepository.findByFio(name);
    }

    @Override
    public void update(Integer id, Person updatedPerson) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isEmpty()) {
            throw new RuntimeException("Person not found");
        }
        Person person = personOptional.get();
        person.setFio(updatedPerson.getFio());
        person.setYearOfBirth(updatedPerson.getYearOfBirth());
        personRepository.save(person);
    }
}
