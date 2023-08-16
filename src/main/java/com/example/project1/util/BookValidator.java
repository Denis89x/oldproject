package com.example.project1.util;

import com.example.project1.model.Book;
import com.example.project1.service.BookServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {

    private final BookServiceImp bookServiceImp;

    @Autowired
    public BookValidator(BookServiceImp bookServiceImp) {
        this.bookServiceImp = bookServiceImp;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        if (bookServiceImp.getByName(book.getName()).isPresent())
            errors.rejectValue("Name", "Книга с таким название уже существует");
    }
}
