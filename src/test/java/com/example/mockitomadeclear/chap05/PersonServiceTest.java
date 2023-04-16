package com.example.mockitomadeclear.chap05;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.mockitomadeclear.chap01.Person;
import com.example.mockitomadeclear.chap01.PersonRepository;
import com.example.mockitomadeclear.chap04.PersonService;

class PersonServiceTest {

    private final List<Person> people = List.of(
        new Person(1, "Grace", "Hopper", LocalDate.of(1906, Month.DECEMBER, 9)),
        new Person(2, "Ada", "Lovelace", LocalDate.of(1815, Month.DECEMBER, 10)),
        new Person(3, "Adele", "Goldberg", LocalDate.of(1945, Month.JULY, 7)),
        new Person(14, "Anita", "Borg", LocalDate.of(1949, Month.JANUARY, 17)),
        new Person(5, "Barbara", "Liskov", LocalDate.of(1939, Month.NOVEMBER, 7)));


    @Test
    void testMockOfFinalMethod() {
        /* Add a directory to your project called mockito-extensions.
            Add a file to `src/main/resources` or `src/test/resources` directory called org.mockito.plugins.MockMaker
            and inside that add the single line mock-maker-inline
            or replace mockito-core in maven/gradle build file to mockito-inline

            The same inline mock maker that Mockito uses to mock final methods can also be used to mock static methods
        * */
        // can mock a class containing final methods
        InMemoryPersonRepo personRepository = mock(InMemoryPersonRepo.class); // u can just use interface in this case for mocking
        when(personRepository.save(any(Person.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        PersonService personService = new PersonService(personRepository);

        List<Integer> ids = personService.savePeople(people.toArray(Person[]::new));
        assertThat(ids).containsExactly(1,2,3,14,5);

        verify(personRepository, times(5)).save(any(Person.class));
    }
}
