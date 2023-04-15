package com.example.mockitomadeclear.chap02;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class) // if using annotations, tell Mockito to process them
// This extension initializes the mocks, and, as a side note, “handles strict stubbings->you can mock only methods that your method under test actually calls.
public class PersonServiceAbbreviatedTest {

    @Mock // to any dependencies to class under test
    private PersonRepository repository;

    @InjectMocks // create mocks, do its best to inject them to class under test
    private PersonServiceAbbreviated personServiceAbbreviated;

    @Captor
    private ArgumentCaptor<Person> personArg;

    private final List<Person> people = List.of(
        new Person(1, "Grace", "Hopper", LocalDate.of(1906, Month.DECEMBER, 9)),
        new Person(2, "Ada", "Lovelace", LocalDate.of(1815, Month.DECEMBER, 10)),
        new Person(3, "Adele", "Goldberg", LocalDate.of(1945, Month.JULY, 7)),
        new Person(14, "Anita", "Borg", LocalDate.of(1949, Month.JANUARY, 17)),
        new Person(5, "Barbara", "Liskov", LocalDate.of(1939, Month.NOVEMBER, 7)));

    private final Map<Integer, Person> peopleMap = people.stream().collect(Collectors.toMap(Person::getId, p->p));



    // // cant be done as junit5 extension is _strict and most of these tests dont call repository.findAll()
    // @BeforeEach
    // void setUp() {
    //     when(repository.findAll()).thenReturn(people);
    // }

    @Test
    void getLastNames_usingMockMethod() {
        // create a stub for the PersonRepository
        PersonRepository mockrepo = mock(PersonRepository.class); // @Mock can also be used
        // Set the expectations on the mock...
        when(mockrepo.findAll()).thenReturn(people);

        // Inject the mock into the service
        PersonServiceAbbreviated personServiceAbbreviated1 = new PersonServiceAbbreviated(mockrepo); //@InjectMocks

        // Get the last names (this is the method to test)
        List<String> lastNames = personServiceAbbreviated1.getLastNames();

        // Check that the last names are correct (using AssertJ)
        assertThat(lastNames).contains("Borg", "Goldberg", "Hopper", "Liskov", "Lovelace");

         // Verify that the service called findAll on the mockRepo exactly once
        verify(mockrepo).findAll();
    }

    @Test
    void defaultImplementations() {
        assertAll(
            () -> assertNull(repository.save(any(Person.class))), //any instance of Person class
            () -> assertTrue(repository.findById(anyInt()).isEmpty()),
            () -> assertTrue(repository.findAll().isEmpty()), //mockito will try to return empty lists, not null
            () -> assertEquals(0, repository.count())
        );
    }

    @Test
    void findByIds_explicitWhens() {
        when(repository.findById(0)).thenReturn(Optional.of(people.get(0)));
        when(repository.findById(1)).thenReturn(Optional.of(people.get(1)));
        when(repository.findById(2)).thenReturn(Optional.of(people.get(2)));
        when(repository.findById(3)).thenReturn(Optional.of(people.get(3)));
        when(repository.findById(4)).thenReturn(Optional.of(people.get(4)));
        when(repository.findById(5)).thenReturn(Optional.empty());

        List<Person> personList = personServiceAbbreviated.findByIds(0,1,2,3,4,5);
        assertThat(personList).containsExactlyElementsOf(people);
    }

    @Test
    void findByIds_thenReturnWithMulipleArgs() {
        when(repository.findById(anyInt())).thenReturn(
            Optional.of(people.get(0)),
            Optional.of(people.get(1)),
            Optional.of(people.get(2)),
            Optional.of(people.get(3)),
            Optional.of(people.get(4)),
            Optional.empty() // If the method is called more than five times, an empty Optional is returned on that call and all subsequent calls.
        );

        List<Person> personList = personServiceAbbreviated.findByIds(0,1,2,3,4,5);
        assertThat(personList).containsExactlyElementsOf(people);
    }

    void testMultipleCalls() {
        when(repository.findById(anyInt()))
            .thenReturn(Optional.of(people.get(0)))
            .thenThrow(new IllegalArgumentException("Person with id not found"))
            .thenReturn(Optional.of(people.get(1)))
            .thenReturn(Optional.empty());
    }

    @Test
    void deleteAllWithNulls() {
        when(repository.findAll()).thenReturn(Arrays.asList((Person) null));
        // when(repository.delete(null)).thenThrow(RuntimeException.class); // void method, hence dont even compile
        doThrow(RuntimeException.class).when(repository).delete(null);
        assertThrows(RuntimeException.class, ()->personServiceAbbreviated.deleteAll());
        verify(repository, times(1)).delete((Person)null);
    }
    //doReturn, doThrow, doAnswer, doNothing, doCallRealMethod - most people use them only for methods that return void.
}
// @InjectsMocks - calls the last constructor on test class, if it has the right types of args
// calls setter methods of proper type(and proper name if there is more than one of a given type)
// sets the fields directly