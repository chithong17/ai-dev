package com.cobol.airlines;

import com.cobol.airlines.controllers.FlightSearchController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FlightSearchTests {

    @Autowired
    private FlightSearchController flightSearchController;

    @Test
    public void testValidSearch() {
        ResponseEntity<List<Map<String, String>>> response = flightSearchController.searchFlights("NYC", "LAX", "2025-11-02");

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("AA123", response.getBody().get(0).get("flightNumber"));
    }

    @Test
    public void testNoResults() {
        ResponseEntity<List<Map<String, String>>> response = flightSearchController.searchFlights("NYC", "SFO", "2025-11-02");

        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }
}