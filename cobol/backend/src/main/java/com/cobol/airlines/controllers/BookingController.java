package com.cobol.airlines.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @PostMapping
    public ResponseEntity<Map<String, String>> createBooking(@RequestBody Map<String, String> bookingDetails) {
        String flightId = bookingDetails.get("flightId");
        String customerId = bookingDetails.get("customerId");
        String seatNumber = bookingDetails.get("seatNumber");

        // Mock booking logic
        if (flightId != null && customerId != null && seatNumber != null) {
            Map<String, String> response = new HashMap<>();
            response.put("bookingId", UUID.randomUUID().toString());
            response.put("message", "Booking successful");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid booking details");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}