package com.example.project1.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookImageWrapper {
    private Book book;
    private Image image;

    public BookImageWrapper(Book book, Image image) {
        this.book = book;
        this.image = image;
    }
}
