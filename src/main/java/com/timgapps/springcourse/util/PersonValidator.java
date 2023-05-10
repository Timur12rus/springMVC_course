package com.timgapps.springcourse.util;

import com.timgapps.springcourse.dao.PersonDAO;
import com.timgapps.springcourse.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// необходимо реализовать интерфейс Validator, чтобы исопльзовать возможности spring Validator
@Component
public class PersonValidator implements Validator {

    // чтобы посмотреть есть ли такой человек в БД, нужно подключиться к БД
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    // необходимо дать понять spring'у к какому классу данный валидатор относится, на объектах какого
    // класса этот валидатор можно использовать
    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass); // класс, который передается должен равняться классу Person, в таком случа
        // мы сможем использовать валидатор
    }

    // реализуем метод, который будет вызываться в peopleController'е, и он будет вызываться на том объекте,
    // который приходит в форме
    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        // нужно посмотреть, есть ли человек с таким же email'ом в БД
//        if (personDAO.show(person.getEmail()) != null) {
        if (personDAO.show(person.getEmail()).isPresent()) {
            // вызов метода isPresent() является лучшей альтернативой проверкой на null, проверяем нашли ли человека в таблице
            // в errors хранятся ошибки, кот-ые получены в процессе валидации
            errors.rejectValue("email", "", "This email is already taken");// если нашли чел-ка показываем ошибку
        }
    }
}
