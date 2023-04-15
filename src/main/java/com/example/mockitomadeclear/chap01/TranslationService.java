package com.example.mockitomadeclear.chap01;

public interface TranslationService {

    default String translate(String text, String sourceLanguage, String targetLanguage) {
        return text;
    }

    String translate(String text);
}
