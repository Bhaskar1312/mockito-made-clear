package com.example.mockitomadeclear.chap05;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BioService {
    private final List<String> pageNames;

    public BioService(String... pageNames) {
        this.pageNames = Arrays.asList(pageNames);
    }

    public List<String> getBios() {
        return pageNames.stream()
            .map(WikiUtil::getWikipediaExtract)
            .collect(Collectors.toList());
    }
}
