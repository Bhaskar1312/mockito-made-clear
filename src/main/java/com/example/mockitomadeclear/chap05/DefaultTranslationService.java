package com.example.mockitomadeclear.chap05;

import com.example.mockitomadeclear.chap01.TranslationService;

public class DefaultTranslationService implements TranslationService {
    @Override
    public String translate(String text, String sourceLanguage, String targetLanguage) {
        return text;
    }

    @Override
    public String translate(String text) {
        return text;
    }
}
