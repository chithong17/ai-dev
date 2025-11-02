package com.cobolairlines.controller;

import com.cobolairlines.dto.FlightDTO;
import com.cobolairlines.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) { this.flightService = flightService; }

    @GetMapping("/search")
    public List<FlightDTO> search(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String flightNum,
            @RequestParam(required = false) String depAirport,
            @RequestParam(required = false) String arrAirport
    ) {
        return flightService.search(date, flightNum, depAirport, arrAirport);
    }
}
