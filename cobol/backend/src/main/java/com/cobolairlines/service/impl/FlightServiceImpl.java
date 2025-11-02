package com.cobolairlines.service.impl;

import com.cobolairlines.dto.FlightDTO;
import com.cobolairlines.mapper.FlightMapper;
import com.cobolairlines.model.Flight;
import com.cobolairlines.repo.FlightRepository;
import com.cobolairlines.service.FlightService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public List<FlightDTO> search(LocalDate date, String flightNum, String depAirport, String arrAirport) {
        Specification<Flight> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            if (date != null) preds.add(cb.equal(root.get("flightDate"), date));
            if (flightNum != null && !flightNum.isEmpty()) preds.add(cb.equal(root.get("flightNum"), flightNum));
            if (depAirport != null && !depAirport.isEmpty()) preds.add(cb.equal(root.get("airportDep"), depAirport));
            if (arrAirport != null && !arrAirport.isEmpty()) preds.add(cb.equal(root.get("airportArr"), arrAirport));
            return cb.and(preds.toArray(new Predicate[0]));
        };

        List<Flight> rows = flightRepository.findAll(spec).stream().limit(10).collect(Collectors.toList());
        return rows.stream().map(FlightMapper::toDto).collect(Collectors.toList());
    }
}
