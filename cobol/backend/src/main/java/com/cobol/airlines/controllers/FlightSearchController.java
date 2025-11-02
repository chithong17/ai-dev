package com.cobol.airlines.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/flights")
public class FlightSearchController {

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> searchFlights(@RequestParam String origin, @RequestParam String destination, @RequestParam String date) {
        // Mock flight search logic
        List<Map<String, String>> flights = new ArrayList<>();

        if ("NYC".equalsIgnoreCase(origin) && "LAX".equalsIgnoreCase(destination) && "2025-11-02".equals(date)) {
            Map<String, String> flight = new HashMap<>();
            flight.put("flightNumber", "AA123");
            flight.put("origin", "NYC");
            flight.put("destination", "LAX");
            flight.put("date", "2025-11-02");
            flights.add(flight);
        }

        return ResponseEntity.ok(flights);
    }
}