package com.example.mockitomadeclear.chap03;


import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.swing.ListSelectionModel;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

class ListCustomMatcher {
    class ListOfTwoElements implements ArgumentMatcher<List> {

        @Override
        public boolean matches(List list) {
            return list.size()==2;
        }

        @Override
        public String toString() {
            return "[ List of 2 elements ]";
        }
    }

    @Test
    void customArgMatcher_withoutLambda() {
        // create mock
        List mock = mock(List.class);
        //set expectations using argThat and customMatcher
        when(mock.addAll(argThat(new ListOfTwoElements()))).thenReturn(true);

        // test a method that invokes addALL with two element list
        mock.addAll(Arrays.asList("one", "two"));

        //verify that test called addAll with customMatcher
        verify(mock).addAll(argThat(new ListOfTwoElements()));

    }

    @Test
    void customArgMatcher_withLambda() {
        List mock = mock(List.class);
        mock.addAll(Arrays.asList("one", "two"));
        verify(mock).addAll(argThat(list -> list.size()==2));
    }
}
