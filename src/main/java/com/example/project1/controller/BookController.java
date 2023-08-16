package com.example.project1.controller;

import com.dropbox.core.DbxException;
import com.example.project1.model.Book;
import com.example.project1.model.BookImageWrapper;
import com.example.project1.model.Image;
import com.example.project1.model.Person;
import com.example.project1.service.DropboxService;
import com.example.project1.service.PersonServiceImp;
import com.example.project1.util.BookValidator;

import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import com.example.project1.service.BookServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookServiceImp bookServiceImp;
    private final BookValidator bookValidator;
    private final PersonServiceImp personServiceImp;
    private final DropboxService dropboxService;


    @Autowired
    public BookController(BookServiceImp bookServiceImp, BookValidator bookValidator,
                          PersonServiceImp personServiceImp, DropboxService dropboxService) {
        this.bookServiceImp = bookServiceImp;
        this.bookValidator = bookValidator;
        this.personServiceImp = personServiceImp;
        this.dropboxService = dropboxService;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("books", bookServiceImp.getAll());
        return "book/index";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        Book book = new Book();
        Image image = new Image();
        BookImageWrapper wrapper = new BookImageWrapper(book, image);
        model.addAttribute("bookImageWrapper", wrapper);
        model.addAttribute("book", book);
        model.addAttribute("image", image);
        return "book/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("bookImageWrapper") @Valid BookImageWrapper bookImageWrapper,
                         BindingResult bindingResult) {
        Book book = bookImageWrapper.getBook();
        Image image = bookImageWrapper.getImage();
        bookValidator.validate(book, bindingResult);

        if (bookServiceImp.isBookNameExists(book.getImageBookName())) {
            bindingResult.rejectValue("book.imageBookName", "error.book.imageBookName", "Изображение с таким названием уже существует");
        }

        if (bindingResult.hasErrors())
            return "book/new";

        try {
            String imageName = dropboxService.saveImage(image.getFile(), image.getImageName());
            book.setImageBookName(image.getImageName());
            String imageUrl = dropboxService.getImageUrl(imageName); // Получаем URL изображения
            book.setImageUrl(imageUrl); // Сохраняем URL изображения в книгу
            System.out.println("imageBookName: " + book.getImageBookName().length());
            System.out.println("imageUrl: " + book.getImageUrl().length());
            bookServiceImp.save(book);
            return "redirect:/book";
        } catch (IOException | DbxException e) {
            e.printStackTrace();
            return "redirect:/book";
        }
    }

        @GetMapping("/{id}")
        public String show(@PathVariable("id") Integer id, Model model, @ModelAttribute("person") Person person) {
            Book book = bookServiceImp.getById(id);
            model.addAttribute("book", book);

            Optional<Person> bookOwner = bookServiceImp.getBookOwner(id);

            if (bookOwner.isPresent())
                model.addAttribute("owner", bookOwner.get());
            else
                model.addAttribute("people", personServiceImp.getAll());

            model.addAttribute("imageUrl", book.getImageUrl());

            return "book/show";
        }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("book", bookServiceImp.getById(id));
        return "book/edit";
    }

    @PostMapping("/{id}/update")
    public String updatePerson(@PathVariable("id") Integer id,
                               @ModelAttribute("book") @Valid Book book,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        bookServiceImp.update(id, book);
        return "redirect:/book";
    }

    @PostMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        bookServiceImp.delete(id);
        return "redirect:/book";
    }

    @PostMapping("/{id}/release")
    public String release(@PathVariable("id") Integer id) {
        bookServiceImp.setBookReleaseStatus(id, true);
        return "redirect:/book/" + id;
    }

    @PostMapping("/{id}/assign")
    public String assign(@PathVariable("id") Integer id, @ModelAttribute("person") Person selectedPerson) {
        bookServiceImp.assign(id, selectedPerson);
        return "redirect:/book/" + id;
    }
}