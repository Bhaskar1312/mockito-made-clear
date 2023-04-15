package com.example.mockitomadeclear.chap01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HelloMockitoRevisedTest {

    @Mock
    private PersonRepository personRepository; //we can test these dependencies even before implementing them

    @Mock
    private TranslationService translationService;

    @InjectMocks
    private HelloMockitoRevised helloMockitoRevised;

    @Test
    @DisplayName("Greet Admiral Hopper")
    void greetAPersonThatExists() {
        // set expectations on the mocks
        when(personRepository.findById(anyInt()))
            .thenReturn(Optional.of(
                new Person(1, "Grace", "Hopper", LocalDate.of(2023, Month.APRIL, 15)
                )));
        when(translationService.translate("Hello, Grace, from Mockito!", "en", "en"))
            .thenReturn("Hello, Grace, from Mockito!");

        // test the greet method
        String greeting = helloMockitoRevised.greet(1, "en", "en");
        assertEquals("Hello, Grace, from Mockito!", greeting);

        // verify the methods are called once, in the right order
        InOrder inOrder = inOrder(personRepository, translationService);
        inOrder.verify(personRepository).findById(anyInt());
        inOrder.verify(translationService).translate(anyString(), eq("en"), eq("en"));
    }

    @Test
    @DisplayName("Greet a Person not in database")
    void greetAPersonThatDoesNotExist() {
        when(personRepository.findById(anyInt()))
            .thenReturn(Optional.empty());
        when(translationService.translate("Hello, World, from Mockito!", "en", "en"))
            .thenReturn("Hello, World, from Mockito!");

        String greeting = helloMockitoRevised.greet(100, "en", "en");
        assertEquals("Hello, World, from Mockito!", greeting);

        InOrder inOrder = inOrder(personRepository, translationService);
        inOrder.verify(personRepository).findById(anyInt());
        inOrder.verify(translationService).translate(anyString(), eq("en"), eq("en"));

    }

    /*
    *   Creating stubs to stand in for the dependencies.
        Setting expectations on the stubs to do what you want.
        Injecting the stubs into the class you’re planning to test.
        Testing the methods in the class under test by invoking its methods, which in turn call methods on the stubs.
        Checking the methods work as expected.
        Verifying that the methods on the dependencies got invoked the right number of times, in the right order.
    * */

}

