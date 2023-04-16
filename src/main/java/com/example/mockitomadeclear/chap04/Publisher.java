package com.example.mockitomadeclear.chap04;

import java.util.ArrayList;
import java.util.List;

// adapted from similar example in Spock framework
public class Publisher {
    private final List<Subscriber> subscribers = new ArrayList<>();

    public void addSubscriber(Subscriber sub) {
        subscribers.add(sub);
    }

    // want to test this method
    public void send(String message) {
        for(Subscriber sub: subscribers) {
            try {
                sub.receive(message);
            } catch (Exception ignored) {
                // evil, we cant even test for exceptions
            }
        }
    }
}
