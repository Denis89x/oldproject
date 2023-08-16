package com.example.project1.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "book")
@Getter
@Setter
@ToString
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @Column(name = "name")
    @Size(min = 3, max = 100, message = "Имя должно быть в диапазоне от 3 до 100 символов")
    private String name;

    @Column(name = "author")
    @Size(min = 3, max = 100, message = "Имя автора должно в диапазоне от 3 до 100 символов")
    private String author;

    @Column(name = "year_of_production")
//    @Min(value = 1810, message = "Минимальный год выпуска 1810")
    private int yearOfProduction;

    @Column(name = "release")
    private Boolean release;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;

    @Column(name = "image_book_name")
    private String imageBookName;

    @Column(name = "image_url")
    private String imageUrl;
}
