package com.example.mockitomadeclear.chap01.astro;

import java.util.Map;
import java.util.stream.Collectors;

public class AstroService {

    private final Gateway<AstroResponse> gateway;
    public AstroService(Gateway<AstroResponse> astroGateway) {
        this.gateway = astroGateway;
    }

    public Map<String, Long>  getAstroData() {
        AstroResponse response = gateway.getResponse();
        return groupByCraft(response);
    }

    private Map<String, Long> groupByCraft(AstroResponse data) {
        return data.getPeople().stream()
            .collect(Collectors.groupingBy(
                    Assignment::getCraft, Collectors.counting()
                ));
    }
}
