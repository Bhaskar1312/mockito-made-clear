package com.example.mockitomadeclear.chap04;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.mockitomadeclear.chap01.Person;
import com.example.mockitomadeclear.chap01.PersonRepository;
import com.example.mockitomadeclear.chap02.InMemoryPersonRepository;
import com.example.mockitomadeclear.chap02.PersonServiceAbbreviated;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    PersonRepository repository;

    @InjectMocks
    PersonServiceAbbreviated service;

    private final List<Person> people = List.of(
        new Person(1, "Grace", "Hopper", LocalDate.of(1906, Month.DECEMBER, 9)),
        new Person(2, "Ada", "Lovelace", LocalDate.of(1815, Month.DECEMBER, 10)),
        new Person(3, "Adele", "Goldberg", LocalDate.of(1945, Month.JULY, 7)),
        new Person(14, "Anita", "Borg", LocalDate.of(1949, Month.JANUARY, 17)),
        new Person(5, "Barbara", "Liskov", LocalDate.of(1939, Month.NOVEMBER, 7)));

    @Test
    void findMaxId() {
        when(repository.findAll()).thenReturn(people);
        assertThat(service.getHighestId()).isEqualTo(14);
        verify(repository).findAll();
    }

    @Test
    void findMaxId_BDD() {
        given(repository.findAll()).willReturn(people);
        assertThat(service.getHighestId()).isEqualTo(14);
        then(repository).should().findAll();
        // then(repository).should(times(1)).findAll(); // == verify(mock, times(1)).methodCall()
    }

    @Captor
    private ArgumentCaptor<Person> personArg; // powerful

    @Test
    void createPersonUsingDateString() {
        PersonService service1 = new PersonService(repository); // different than the one using @Mock

        Person hopper = people.get(0);
        when(repository.save(hopper)).thenReturn(hopper);
        Person actual = service1.createPerson(1, "Grace", "Hopper", "1906-12-09");

        verify(repository).save(personArg.capture());
        assertThat(personArg.getValue()).isEqualTo(hopper);
        assertThat(actual).isEqualTo(hopper);
    }
    /*
    * Argument captors allow you to find out exactly what arguments were supplied to a mocked method, even when that argument is created as a local variable in the implementation.
    * */


    @Test
    void saveAllPeople() {
        when(repository.save(any(Person.class)))
            .thenReturn(people.get(0),
                        people.get(1),
                        people.get(2),
                        people.get(3),
                        people.get(4) // 5th call and all subsequent calls return this
                );

        // test the service which uses the mock
        assertEquals(List.of(1,2,3,14,5), service.savePeople(people.toArray(Person[]::new)));

        //verify interaction between service and mock
        verify(repository, times(people.size())).save(any(Person.class));
        verify(repository, never()).delete(any(Person.class));
    }

    @Test // powerful -> use Answer if output of mocked methods to be based on their input args
    void useAnswer() {
        // lambda method implementation of Answer<Person>
        when(repository.save(any(Person.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        // when(repository.save(any(Person.class)))
        //     .thenAnswer(invocation -> {
        //         System.out.println(">>>"+Arrays.stream(invocation.getArguments()).collect(Collectors.toList()));
        //         return invocation.getArgument(0);
        //     });

        List<Integer> ids = service.savePeople(people.toArray(Person[]::new));

        List<Integer> actuals = people.stream()
            .map(Person::getId)
            .collect(Collectors.toList());
        assertEquals(ids, actuals);
    }

    @Test // spy on repo to verify times without a field
    void testInMemoryPersonRepository() {
        PersonRepository personRepository = new InMemoryPersonRepository();
        PersonService personService = new PersonService(personRepository);

        personService.savePeople(people.toArray(Person[]::new));
        assertThat(personRepository.findAll()).isEqualTo(people);
    }

    @Test
    void spyOnRepository() {
        // spy on the in-memory repo
        PersonRepository personRepository = spy(new InMemoryPersonRepository());
        PersonService personService = new PersonService(personRepository);

        personService.savePeople(people.toArray(Person[]::new));
        assertThat(personRepository.findAll()).isEqualTo(people);

        // verify the method calls on spy
        verify(personRepository, times(people.size())).save(any(Person.class));
    }
    /*
    1. You can intercept method calls to the dependencies for later verification.
    2. You can mock some methods in the dependencies rather than all of them. This is called a partial mock.
    you don’t want to use partial mocks because their state is not maintained between the mocked methods and the non-mocked methods
    * */

}
