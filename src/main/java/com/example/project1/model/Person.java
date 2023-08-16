package com.example.project1.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Table(name = "person")
@Entity
@Setter
@Getter
@ToString
public class Person {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;

    @Column(name = "fio")
    @NotEmpty(message = "ФИО не должно быть пустым")
    @Size(min = 5, max = 100, message = "ФИО должно быть от 5 до 100 символов")
    private String fio;

    @Column(name = "year_of_birth")
    @Min(value = 1910, message = "Год рождения должен быть от 1910г.")
    private int yearOfBirth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;
}
