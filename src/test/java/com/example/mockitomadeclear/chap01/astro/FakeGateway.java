package com.example.mockitomadeclear.chap01.astro;

import java.util.List;

public class FakeGateway implements Gateway<AstroResponse> {
    @Override
    public AstroResponse getResponse() {
        return new AstroResponse(7, "Success",
            List.of(new Assignment("Kathryn Janeway", "USS Voyager"),
                new Assignment("Seven of Nine", "USS Voyager"),
                new Assignment("Will Robinson", "Jupiter 2"),
                new Assignment("Lennier", "Babylon 5"),
                new Assignment("James Holden", "Rocinante"),
                new Assignment("Naomi Negata", "Rocinante"),
                new Assignment("Ellen Ripley", "Nostromo")));
    }

}
