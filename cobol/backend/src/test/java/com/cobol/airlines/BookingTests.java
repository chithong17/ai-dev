package com.cobol.airlines;

import com.cobol.airlines.controllers.BookingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingTests {

    @Autowired
    private BookingController bookingController;

    @Test
    public void testValidBooking() {
        Map<String, String> bookingDetails = new HashMap<>();
        bookingDetails.put("flightId", "12345");
        bookingDetails.put("customerId", "67890");
        bookingDetails.put("seatNumber", "12A");

        ResponseEntity<Map<String, String>> response = bookingController.createBooking(bookingDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Booking successful", response.getBody().get("message"));
    }

    @Test
    public void testInvalidBooking() {
        Map<String, String> bookingDetails = new HashMap<>();
        bookingDetails.put("flightId", "");
        bookingDetails.put("customerId", "67890");
        bookingDetails.put("seatNumber", "12A");

        ResponseEntity<Map<String, String>> response = bookingController.createBooking(bookingDetails);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid booking details", response.getBody().get("message"));
    }
}