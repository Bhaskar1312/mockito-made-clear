package com.example.mockitomadeclear.chap01.astro;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.Test;

class AstroServiceTest {

    AstroService astroService;

    // an integration test is one that tests a system including components it interacts with (dependencies)
    @Test
    void testAstroData_usingRealGateway_withHttpClient() {
        // Create an instance of AstroService using the real Gateway
        astroService = new AstroService(new AstroGateway());

        //  Call the method under test
        Map<String, Long> astroData = astroService.getAstroData();

        // Print the results and check that they are reasonable
        astroData.forEach((craft, number) -> {
            System.out.println(number+" astronauts aboard "+craft);
            assertAll(
                () -> assertThat(number).isPositive(),
                () -> assertThat(craft).isNotBlank()
            );
        });
    }

    @Test
    void testAstroData_usingOwnFakeGateway() {
        // create the service using the mock gateway
        astroService = new AstroService(new FakeGateway());

        // call the method under test
        Map<String, Long> astroData = astroService.getAstroData();

        // check the results from method under test
        astroData.forEach((craft, number) -> {
            System.out.println(number+" astronauts aboard "+craft);
            assertAll(
                () -> assertThat(number).isPositive(),
                () -> assertThat(craft).isNotBlank()
            );
        });
    }
}
