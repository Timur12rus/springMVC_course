package com.timgapps.springcourse.dao;

import com.timgapps.springcourse.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT *  FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    // new Person or null
    // если хотя бы один объект найден в списке он будет возвращен,
    // если объекта нет - возвращаем null
    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name, age, email) VALUES(?, ?, ?)",
                person.getName(),
                person.getAge(),
                person.getEmail());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=? WHERE id=?",
                updatedPerson.getName(),
                updatedPerson.getAge(),
                updatedPerson.getEmail(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    //////////////////////////////////////////
    // Тестируем производительность пакетной вставки
    ////////////////////////////////////////////////////

    // метод будет вставлять в БД 1000 людей с помощью обычнго INSERT
    public void testMultipleUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis(); // время до вставки данных в БД

        for (Person person : people) {
            jdbcTemplate.update("INSERT INTO Person VALUES(?, ?, ?, ?)",
                    person.getId(), person.getName(), person.getAge(), person.getEmail());
        }

        long after = System.currentTimeMillis();  // время после вставки данных в БД

        System.out.println("Time: " + (after - before));
    }

    // метод будет вставлять в БД 1000 людей одним пакетом
    public void testBatchUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis(); // время до вставки данных в БД

        // вставка с момощью специального метода
        jdbcTemplate.batchUpdate("INSERT INTO Person VALUES(?, ?, ?, ?)",
                // анонимный класс с двумя методами, которые мы должны реализовать
                new BatchPreparedStatementSetter() {
                    @Override
                    // здесь передаем все значения, котрые попадут в пакет для передачи данных в БД
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setInt(1, people.get(i).getId());
                        preparedStatement.setString(2, people.get(i).getName());
                        preparedStatement.setInt(3, people.get(i).getAge());
                        preparedStatement.setString(4, people.get(i).getEmail());
                    }

                    @Override
                    // здесь должны вернуть размер нашего пакета
                    public int getBatchSize() {
                        return people.size();
                    }
                });

        long after = System.currentTimeMillis();  // время после вставки данных в БД

        System.out.println("Time: " + (after - before));
    }

    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i, "Name" + i, 30, "test" + i + "@mail.ru"));
        }
        return people;
    }
}
