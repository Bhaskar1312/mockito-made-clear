package com.example.mockitomadeclear.chap05;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.junit.MockitoJUnitRunner;
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

// If you plan to use both JUnit 5 and JUnit 4 tests in the same project, be sure to include the vintage engine with your JUnit dependencies:

// Junit4 - has 3 ways to tell to work with annotations
// 1. @RunWith(MockitoJUnitRunner.class)
// 2. @Rule public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS); // strict -default for junit5 , lenient for junit-4
// 3. 	@Before public void setUp() { MockitoAnnotations.openMocks(this); }