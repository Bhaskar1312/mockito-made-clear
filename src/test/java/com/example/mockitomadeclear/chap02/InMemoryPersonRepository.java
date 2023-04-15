package com.example.mockitomadeclear.chap02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.mockitomadeclear.chap01.Person;
import com.example.mockitomadeclear.chap01.PersonRepository;

public class InMemoryPersonRepository implements PersonRepository {

    private final List<Person> people = Collections.synchronizedList(new ArrayList<>());
    @Override
    public Person save(Person person) {
        synchronized (people) {
            people.add(person);
        }
        return person;
    }

    @Override
    public Optional<Person> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Person> findAll() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Person person) {
        synchronized (people) {
            people.remove(person);
        }
    }
}
