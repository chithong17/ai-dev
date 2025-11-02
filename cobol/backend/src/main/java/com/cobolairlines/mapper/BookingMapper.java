package com.cobolairlines.mapper;

import com.cobolairlines.dto.BookingQuote;
import com.cobolairlines.model.Flight;
import com.cobolairlines.model.Passenger;

import java.math.BigDecimal;

public class BookingMapper {
    private static final BigDecimal UNIT_PRICE = new BigDecimal("120.99");

    public static BookingQuote toQuote(Passenger p, Flight f, int passengerCount) {
        BookingQuote q = new BookingQuote();
        if (p != null) {
            q.setClientId(p.getClientId());
            q.setClientFirstName(p.getFirstName());
            q.setClientLastName(p.getLastName());
        }
        if (f != null) {
            q.setFlightId(f.getFlightId());
            q.setFlightNum(f.getFlightNum());
        }
        q.setUnitPrice(UNIT_PRICE);
        q.setPassengerCount(passengerCount);
        BigDecimal total = UNIT_PRICE.multiply(BigDecimal.valueOf(passengerCount));
        q.setTotalPrice(total);
        return q;
    }
}
