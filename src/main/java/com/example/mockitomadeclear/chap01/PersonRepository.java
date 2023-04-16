package com.example.mockitomadeclear.chap01;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository {
    Person save(Person person);
    Optional<Person> findById(int id);
    List<Person> findAll();
    long count();
    void delete(Person person);
}
