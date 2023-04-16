package com.example.mockitomadeclear.chap04;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.mockitomadeclear.chap01.Person;
import com.example.mockitomadeclear.chap01.PersonRepository;

public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public Person createPerson(int id, String first, String last, LocalDate dob) {
        Person person = new Person(id, first, last, dob);
        return repository.save(person);
    }

    public Person createPerson(int id, String first, String last, String dobString) {
        Person person = new Person(id, first, last, LocalDate.parse(dobString));
        return repository.save(person);
    }

    public List<Integer> savePeople(Person... person) {
        return Arrays.stream(person)
            .map(repository::save)
            .map(Person::getId)
            .collect(Collectors.toList());
    }

    public long getTotalPeople() {
        return repository.count();
    }

}
