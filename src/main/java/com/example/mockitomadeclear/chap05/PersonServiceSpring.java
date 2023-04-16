package com.example.mockitomadeclear.chap05;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mockitomadeclear.chap01.Person;
import com.example.mockitomadeclear.chap01.PersonRepository;

@Service // can only autowire into a managed bean
public class PersonServiceSpring {
    private final PersonRepository repository;

    // @Autowired // not required if only one constructor, but it doesn't hurt
    public PersonServiceSpring(PersonRepository repository) {
        this.repository = repository;
    }

    public Integer getHighestId() {
        return repository.findAll().stream().map(Person::getId).max(Integer::compareTo).orElse(0);
    }
}
