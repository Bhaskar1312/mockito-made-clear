package com.example.mockitomadeclear.chap05;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockConstructionWithAnswer;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import com.example.mockitomadeclear.chap01.Person;
import com.example.mockitomadeclear.chap01.PersonRepository;

class HelloMockitoMockedConstructorTest {

    @Test
    // Since version 3.5, Mockito has had the ability to mock constructors. As with mocking static methods,
        // you need to use the mockito-inline dependency to take advantage of it, and you should wrap the mocked object inside a try-with-resources block
    void greetWithMockedConstructor() {
        // Mock for repo (needed for HelloMockito constructor)
        PersonRepository mockRepo = mock(PersonRepository.class);
        when(mockRepo.findById(anyInt()))
            .thenReturn(Optional.of(
                new Person(1, "Grace", "Hopper", LocalDate.now())
            ));

        // mock for translator (instantiated inside HelloMockito constructor)
        try (MockedConstruction<DefaultTranslationService> ignored
                 = mockConstruction(DefaultTranslationService.class,
                    (mock, context)-> {
                        when(mock.translate(anyString(), anyString(), anyString()))
                            .thenAnswer(invocation -> invocation.getArgument(0) + " (translated)");
                    })){
            // instantiate HelloMockito with mocked repo and locally instantiated constructor
            HelloMockitoMockedConstructor hello = new HelloMockitoMockedConstructor(mockRepo);
            String greeting = hello.greet(1, "en", "en");
            assertThat(greeting).isEqualTo("Hello, Grace, from Mockito! (translated)");
        }
    }

    @Test
    void greetWithMockedConstructorWithAnswer() {
        PersonRepository mockRepo = mock(PersonRepository.class);
        when(mockRepo.findById(anyInt()))
            .thenReturn(Optional.of(
                new Person(1, "Grace", "Hopper", LocalDate.now())
            ));

        try (MockedConstruction<DefaultTranslationService> ignored
                 = mockConstructionWithAnswer(DefaultTranslationService.class,
                    invocation -> invocation.getArgument(0) + " (translated)")
            ){
            // instantiate HelloMockito with mocked repo and locally instantiated constructor
            HelloMockitoMockedConstructor hello = new HelloMockitoMockedConstructor(mockRepo);
            String greeting = hello.greet(1, "en", "en");
            assertThat(greeting).isEqualTo("Hello, Grace, from Mockito! (translated)");
        }
    }
}
