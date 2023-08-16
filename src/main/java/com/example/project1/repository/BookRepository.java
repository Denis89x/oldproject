package com.example.project1.repository;

import com.example.project1.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByName(String fio);

    Optional<Book> findByImageBookName(String imageName);

    List<Book> findByOwner_personId(Integer personId);

//    void release(Integer id);

    Book release(Integer id);
}
