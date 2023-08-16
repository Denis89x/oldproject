package com.example.project1.controller;

import com.example.project1.model.Book;
import com.example.project1.model.Person;
import com.example.project1.service.BookServiceImp;
import com.example.project1.service.PersonServiceImp;
import com.example.project1.util.PersonValidator;
//import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonServiceImp personServiceImp;
    private final BookServiceImp bookServiceImp;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonServiceImp personServiceImp, BookServiceImp bookServiceImp, PersonValidator personValidator) {
        this.personServiceImp = personServiceImp;
        this.bookServiceImp = bookServiceImp;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", personServiceImp.getAll());
        return "people/index";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        personServiceImp.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("person", personServiceImp.getById(id));
        model.addAttribute("books", bookServiceImp.getBooksByPersonId(id));
        return "people/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("person", personServiceImp.getById(id));
        return "people/edit";
    }

    @PostMapping("/{id}/update")
    public String updatePerson(@PathVariable("id") Integer id,
                               @ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        personServiceImp.update(id, person);
        return "redirect:/people";
    }

    @PostMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        personServiceImp.delete(id);
        return "redirect:/people";
    }
}