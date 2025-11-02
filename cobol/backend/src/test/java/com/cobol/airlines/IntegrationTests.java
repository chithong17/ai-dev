package com.cobol.airlines;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testLogin() {
        webTestClient.post().uri("/login")
            .bodyValue("{\"username\":\"testuser\", \"password\":\"password\"}")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    public void testFlightSearch() {
        webTestClient.get().uri(uriBuilder ->
            uriBuilder.path("/flights")
                      .queryParam("origin", "NYC")
                      .queryParam("destination", "LAX")
                      .queryParam("date", "2025-11-02")
                      .build())
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    public void testBooking() {
        webTestClient.post().uri("/bookings")
            .bodyValue("{\"flightId\":\"1\", \"customerId\":\"1\", \"seatNumber\":\"12A\"}")
            .exchange()
            .expectStatus().isOk();
    }
}