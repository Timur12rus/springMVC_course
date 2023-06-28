package com.timgapps.springcourse.repositories;

import com.timgapps.springcourse.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// здесь integer - тип первичного ключа
@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

}
