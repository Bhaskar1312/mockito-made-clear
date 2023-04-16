package com.example.mockitomadeclear.chap05;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class BioServiceTest {

    @Test
    //Integration test, needs internet?
    void checkBios() {
        BioService service = new BioService(
            "Anita Borg", "Ada Lovelace", "Grace Hopper", "Barbara Liskov");

        assertThat(service.getBios()).hasSize(4);
    }

    @Test
    void testBioServiceWithMocks_static() {
        // the mockStatic method creates a thread-local controller for all static methods in the supplied class or interface and that you should call the close method on it when the test completes
        BioService service = new BioService(
            "Anita Borg", "Ada Lovelace", "Grace Hopper", "Barbara Liskov");

        // use mockStatic in a try-resources block
        try (MockedStatic<WikiUtil> mocked = mockStatic(WikiUtil.class)){
            // same when/then as a regular mock
            mocked.when(()->WikiUtil.getWikipediaExtract(anyString()))
                .thenAnswer(invocation -> "Bio for "+ invocation.getArgument(0));

            assertThat(service.getBios()).hasSize(4);

            // verify using a mockedStatic Verification argument
            mocked.verify(()->WikiUtil.getWikipediaExtract(anyString()), times(4));
        }
    }
}
