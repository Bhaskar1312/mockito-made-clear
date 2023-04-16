package com.example.mockitomadeclear.chap04;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class PublisherTest {
    private final Publisher pub = new Publisher();
    private final Subscriber sub1 = mock(Subscriber.class);
    private final Subscriber sub2 = mock(Subscriber.class);

    @BeforeEach
    void setUp() {
        pub.addSubscriber(sub1);
        pub.addSubscriber(sub2);
    }

    @Test
    void publisherSendsMessageToAllSubscribers() {
        pub.send("Hello");

        verify(sub1).receive("Hello");
        verify(sub2).receive("Hello");
    }

    @Test
    void testSendInOrder() {
        pub.send("Hello");

        InOrder inOrder = inOrder(sub1, sub2);
        inOrder.verify(sub1).receive("Hello");
        inOrder.verify(sub2).receive("Hello");
    }

    @Test
    void publisherSendsMessageWithAPattern() {
        pub.send("Message 1");
        pub.send("Message 2");

        // check for anyString
        verify(sub1, times(2)).receive(anyString());
        verify(sub2, times(2)).receive(anyString());

        // alternatively check for a specific pattern
        verify(sub1, times(2)).receive(argThat(s -> s.matches("Message \\d")));
        verify(sub2, times(2)).receive(argThat(s -> s.matches("Message \\d")));
    }

    @Test
    void handleMisbehavingSubscribers() {
        // sub1 throws an exception, void method -> use doThrow, doNothing
        doThrow(RuntimeException.class).when(sub1).receive(anyString());

        pub.send("message 1");
        pub.send("message 2");

        // verify whether subscribers still receive the message
        verify(sub1, times(2)).receive(anyString());
        verify(sub2, times(2)).receive(anyString());
    }
}
