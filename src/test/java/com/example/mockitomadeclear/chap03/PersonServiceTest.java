package com.example.mockitomadeclear.chap03;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.mockitomadeclear.chap01.Person;
import com.example.mockitomadeclear.chap01.PersonRepository;
import com.example.mockitomadeclear.chap02.PersonServiceAbbreviated;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    PersonRepository repository;

    @InjectMocks
    PersonServiceAbbreviated personServiceAbbreviated;

    @Test
    void findByIdThatDoesNotExist() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        List<Person> personList = personServiceAbbreviated.findByIds(999);

        assertTrue(personList.isEmpty());
        verify(repository).findById(anyInt());
    }

    @Test
    @Disabled("Do not use argThat with primitives")
    void findByIdsThatDoNotExist_argThat() {
        when(repository.findById(argThat(id -> id > 14))).thenReturn(Optional.empty());
        List<Person> personList = personServiceAbbreviated.findByIds(15, 42, 78, 999);
        assertTrue(personList.isEmpty());
        verify(repository, times(4)).findById(anyInt());
    }

    @Test
    // NullPointerException auto-unboxing caveat. In rare cases when matching primitive parameter types you must use relevant intThat(), floatThat(), etc. method. This way you will avoid NullPointerException during auto-unboxing.
    void findByIdsThatDoNotExist_intThat() {
        when(repository.findById(intThat(id -> id > 14))).thenReturn(Optional.empty());
        List<Person> personList = personServiceAbbreviated.findByIds(15, 42, 78, 999);
        assertTrue(personList.isEmpty());
        verify(repository, times(4)).findById(anyInt());
    }
}
/*
Methods that match any instance of primitive types: anyByte, anyShort, anyInt, anyLong, anyFloat, anyDouble, anyChar, and anyBoolean. These methods work exactly the way you would expect.
Methods that match collections, like anyCollection, anyList, anySet, and anyMap. These methods match any non-null collection of the specified type.
Methods that work on strings, like anyString, startsWith, endsWith, and the two overloads of matches, one that takes a regular expression as a string and the other a Pattern.
General-purpose matchers for nulls, like isNull and isNotNull (and its companion, notNull, which is just an alias), and nullable(Class), which matches either null or a given type.
For types, the matcher isA(Class) matches any argument that is assignable to a reference of the specified class. That leads to any(Class), which matches any instance of the specified type, excluding nulls, and the ultimate matcher, any, which matches anything, including nulls and varargs.
* */
