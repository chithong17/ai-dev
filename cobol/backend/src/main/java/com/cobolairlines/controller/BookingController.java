package com.cobolairlines.controller;

import com.cobolairlines.dto.BookingQuote;
import com.cobolairlines.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) { this.bookingService = bookingService; }

    public static class ValidateRequest {
        public String clientId;
        public String flightNum;
        public LocalDate date;
        public Integer passengerCount;
    }

    @PostMapping("/validate-step1")
    public ResponseEntity<?> validate(@RequestBody ValidateRequest req) {
        try {
            BookingQuote q = bookingService.validateStep1(req.clientId, req.flightNum, req.date, req.passengerCount == null ? 1 : req.passengerCount);
            return ResponseEntity.ok(Map.of("bookingQuote", q));
        } catch (BookingService.BookingException e) {
            String msg = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", msg));
        }
    }
}
