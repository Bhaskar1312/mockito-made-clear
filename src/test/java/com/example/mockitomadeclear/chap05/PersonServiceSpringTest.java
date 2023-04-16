package com.example.mockitomadeclear.chap05;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.mockitomadeclear.chap01.Person;
import com.example.mockitomadeclear.chap01.PersonRepository;

@SpringBootTest
class PersonServiceSpringTest {
//Spring also has a similar annotation called @SpyBean. That annotation wraps a Mockito spy around an existing instance of the field.
    @MockBean // instantiate mock and put it into application context
    private PersonRepository repository;

    PersonServiceSpring service = new PersonServiceSpring(repository);

    private final List<Person> people = List.of(
        new Person(1, "Grace", "Hopper", LocalDate.of(1906, Month.DECEMBER, 9)),
        new Person(2, "Ada", "Lovelace", LocalDate.of(1815, Month.DECEMBER, 10)),
        new Person(3, "Adele", "Goldberg", LocalDate.of(1945, Month.JULY, 7)),
        new Person(14, "Anita", "Borg", LocalDate.of(1949, Month.JANUARY, 17)),
        new Person(5, "Barbara", "Liskov", LocalDate.of(1939, Month.NOVEMBER, 7)));

    @Test // failing
    void findMaxId() {
        // set expectations on mock
        when(repository.findAll()).thenReturn(people);

        // test service method
        assertEquals(14, service.getHighestId().intValue());

        //  verify method call on mock
        verify(repository).findAll();
    }
}
