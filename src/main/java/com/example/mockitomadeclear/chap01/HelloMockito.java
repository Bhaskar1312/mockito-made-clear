package com.example.mockitomadeclear.chap01;

public class HelloMockito {
    private String greeting = "Hello, %s, from Mockito!";

    public String greet(String name) {
        return String.format(greeting, name);
    }
}
