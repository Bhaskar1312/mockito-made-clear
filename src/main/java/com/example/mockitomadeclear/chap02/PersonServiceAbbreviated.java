package com.example.mockitomadeclear.chap02;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.mockitomadeclear.chap01.Person;
import com.example.mockitomadeclear.chap01.PersonRepository;

public class PersonServiceAbbreviated {
    private final PersonRepository repository;

    public PersonServiceAbbreviated(PersonRepository repository) {
        this.repository = repository;
    }

    public List<String> getLastNames() {
        return repository.findAll().stream()
            .map(Person::getLast)
            .collect(Collectors.toList());
    }

    public List<Person> findByIds(int... ids) {
        return Arrays.stream(ids)
            .mapToObj(repository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    public void deleteAll() {
        repository.findAll()
            .forEach(repository::delete);
    }

    public Integer getHighestId() {
        return repository.findAll().stream().map(Person::getId).max(Integer::compareTo).orElse(0);
    }

    public List<Integer> savePeople(Person... person) {
        return Arrays.stream(person)
            .map(repository::save)
            .map(Person::getId)
            .collect(Collectors.toList());
    }
}
