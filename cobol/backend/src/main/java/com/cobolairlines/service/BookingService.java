package com.cobolairlines.service;

import com.cobolairlines.dto.BookingQuote;

import java.time.LocalDate;

public interface BookingService {
    BookingQuote validateStep1(String clientId, String flightNum, LocalDate date, int passengerCount) throws BookingException;

    class BookingException extends Exception {
        public BookingException(String message) { super(message); }
    }
}
