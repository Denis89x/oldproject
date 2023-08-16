package com.example.project1.util;

import com.example.project1.model.Person;
import com.example.project1.service.PersonServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonServiceImp personServiceImp;

    @Autowired
    public PersonValidator(PersonServiceImp personServiceImp) {
        this.personServiceImp = personServiceImp;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (personServiceImp.getByName(person.getFio()).isPresent())
            errors.rejectValue("fio", "Человек с таким ФИО уже существует");
    }
}
