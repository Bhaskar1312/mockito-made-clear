package com.example.mockitomadeclear.chap02.astro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.mockitomadeclear.chap01.astro.Assignment;
import com.example.mockitomadeclear.chap01.astro.AstroResponse;
import com.example.mockitomadeclear.chap01.astro.AstroService;
import com.example.mockitomadeclear.chap01.astro.Gateway;

@ExtendWith(MockitoExtension.class)
class AstroServiceTest {
    @Mock
    private Gateway<AstroResponse> gateway;

    @InjectMocks
    private AstroService service;

    private final AstroResponse mockAstroResponse =
        new AstroResponse(7, "Success", Arrays.asList(
            new Assignment("John Sheridan", "Babylon 5"),
            new Assignment("Susan Ivanova", "Babylon 5"),
            new Assignment("Beckett Mariner", "USS Cerritos"),
            new Assignment("Brad Boimler", "USS Cerritos"),
            new Assignment("Sam Rutherford", "USS Cerritos"),
            new Assignment("D'Vana Tendi", "USS Cerritos"),
            new Assignment("Ellen Ripley", "Nostromo")
        ));

    @Test
    void testAstroData_usingInjectedMockGateway() {
        when(gateway.getResponse()).thenReturn(mockAstroResponse);
        Map<String, Long> astroData = service.getAstroData();

        assertThat(astroData)
            .containsEntry("Babylon 5", 2L)
            .containsEntry("Nostromo", 1L)
            .containsEntry("USS Cerritos", 4L);

        astroData.forEach((craft, number) -> {
            System.out.println(number+" astronauts aboard "+craft);
            assertAll(
                () -> AssertionsForClassTypes.assertThat(number).isPositive(),
                () -> AssertionsForClassTypes.assertThat(craft).isNotBlank()
            );
        });

        verify(gateway).getResponse();
    }

    @Test
    void testAstroData_usingFailedGateway() {
        when(gateway.getResponse()).thenThrow(new RuntimeException(new IOException("Network Problems")));

        assertThatExceptionOfType(RuntimeException.class)
            .isThrownBy(()->service.getAstroData())
            .withCauseInstanceOf(IOException.class)
            .withMessageContaining("Network Problems");
    }
}
