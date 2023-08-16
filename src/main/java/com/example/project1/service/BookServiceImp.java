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
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookServiceImp(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void delete(Integer id) {
        bookRepository.deleteById(id);
    }

    public Book getById(Integer id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new RuntimeException("Book not found");
        }
        return bookOptional.get();
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public void update(Integer id, Book updatedBook) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new RuntimeException("Person not found");
        }
        Book book = bookOptional.get();
        book.setAuthor(updatedBook.getAuthor());
        book.setName(updatedBook.getName());
        book.setYearOfProduction(updatedBook.getYearOfProduction());
        bookRepository.save(book);
    }

    @Override
    public Optional<Book> getByName(String name) {
        return bookRepository.findByName(name);
    }

    @Override
    public List<Book> getBooksByPersonId(Integer personId) {
        return bookRepository.findByOwner_personId(personId);
    }

    @Override
    public void assign(Integer id, Person selectedPerson) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            book.get().setOwner(selectedPerson);
            bookRepository.save(book.get());
        }
    }

    @Override
    public Optional<Person> getBookOwner(Integer id) {
        return bookRepository.findById(id).map(Book::getOwner);
    }

    @Override
    public void setBookReleaseStatus(Integer bookId, boolean isReleased) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setRelease(isReleased);
        book.setOwner(null);
        bookRepository.save(book);
    }

    @Override
    public Boolean isBookNameExists(String name) {
        Optional<Book> book = bookRepository.findByImageBookName(name);
        if (book.isPresent())
            return true;
        return false;
    }
}
