package com.cobolairlines.service;

import com.cobolairlines.dto.FlightDTO;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    List<FlightDTO> search(LocalDate date, String flightNum, String depAirport, String arrAirport);
}
