package com.example.mockitomadeclear.chap05;

import java.util.Optional;

import com.example.mockitomadeclear.chap01.HelloMockito;
import com.example.mockitomadeclear.chap01.Person;
import com.example.mockitomadeclear.chap01.PersonRepository;
import com.example.mockitomadeclear.chap01.TranslationService;

public class HelloMockitoMockedConstructor {
    private final PersonRepository repository;
    private final TranslationService service;
    public HelloMockitoMockedConstructor(PersonRepository personRepository) {
        // this(personRepository, new DefaultTranslationService());
        this.repository = personRepository;
        this.service = new DefaultTranslationService();
    }

    // public HelloMockitoMockedConstructor(PersonRepository repository, TranslationService service) {
    //     this.repository =repository;
    //     this.service = service;
    // }

    private String greeting = "Hello, %s, from Mockito!";

    public String greet(String name) {
        return String.format(greeting, name);
    }

    public String greet(int id, String sourceLanguage, String targetLanguage) {
        Optional<Person> person = repository.findById(id);
        String name = person.map(Person::getFirst).orElse("World");
        return service.translate(
            String.format(greeting, name), sourceLanguage, targetLanguage);
    }

}
