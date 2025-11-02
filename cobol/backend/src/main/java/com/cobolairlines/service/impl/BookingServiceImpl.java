package com.cobolairlines.service.impl;

import com.cobolairlines.dto.BookingQuote;
import com.cobolairlines.mapper.BookingMapper;
import com.cobolairlines.model.Flight;
import com.cobolairlines.model.Passenger;
import com.cobolairlines.repo.FlightRepository;
import com.cobolairlines.repo.PassengerRepository;
import com.cobolairlines.service.BookingService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;

    public BookingServiceImpl(PassengerRepository passengerRepository, FlightRepository flightRepository) {
        this.passengerRepository = passengerRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public BookingQuote validateStep1(String clientId, String flightNum, LocalDate date, int passengerCount) throws BookingException {
        Passenger p = passengerRepository.findByClientId(clientId).orElse(null);
        if (p == null) throw new BookingException("THIS PASSENGER DOES NOT EXIST");

        Specification<Flight> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            if (date != null) preds.add(cb.equal(root.get("flightDate"), date));
            if (flightNum != null && !flightNum.isEmpty()) preds.add(cb.equal(root.get("flightNum"), flightNum));
            return cb.and(preds.toArray(new Predicate[0]));
        };
        List<Flight> flights = flightRepository.findAll(spec);
        if (flights.isEmpty()) throw new BookingException("THIS FLIGHT DOES NOT EXIST");

        Flight f = flights.get(0);
        return BookingMapper.toQuote(p, f, passengerCount);
    }
}
