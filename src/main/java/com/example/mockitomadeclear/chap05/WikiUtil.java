package com.example.mockitomadeclear.chap05;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WikiUtil {
    public static String getWikipediaExtract(String title) {
        String base = "https://en.wikipedia.org/w/api.php";
        Map<String, String> params = Map.ofEntries(
            Map.entry("action", "query"),
            Map.entry("prop", "extracts"),
            Map.entry("format", "json"),
            Map.entry("exintro", "true"),
            Map.entry("explaintext", "1"),
            Map.entry("titles", URLEncoder.encode(title, StandardCharsets.UTF_8)),
            Map.entry("redirects", "1"),
            Map.entry("formatversion", "2")
        );
        String queryString = params.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));
        return getResponse(base + "?" + queryString);
    }

    private static String getResponse(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(2))
            .build();
        try {
            return parseResponse(client.send(request, HttpResponse.BodyHandlers.ofString()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String parseResponse(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        WikiResponse json = mapper.readValue(response.body(), WikiResponse.class);
        WikiPage page = json.getQuery().getPages().get(0);
        return page.getTitle() + ": " + page.getExtract();
    }
}
