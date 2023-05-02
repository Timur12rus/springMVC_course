package com.timgapps.springcourse.dao;

import com.timgapps.springcourse.models.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;
    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL,
                    USERNAME,
                    PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Person> index() {
        List<Person> people = new ArrayList<>();
        try {
            Statement statement = connection.createStatement(); // тот объект, который содержит в себе запрос к базе данных
            String SQL = "SELECT * FROM Person";
            ResultSet resultSet = statement.executeQuery(SQL);    // передаем наш SQL запрос, в этом объекте лежат наши строки

            while(resultSet.next()) {
                Person person = new Person();

                person.setId("id");
                person.setName(resultSet.getString());
                person.setName(resultSet.getString());
                person.setName(resultSet.getString());

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return people;
    }

    public Person show(int id) {
//        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public void save(Person person) {
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }

    public void update(int id, Person updatedPerson) {
//        Person personToBeUpdated = show(id);
//        personToBeUpdated.setName(updatedPerson.getName());
    }

    public void delete(int id) {
//        people.removeIf(p -> p.getId() == id);
    }
}
