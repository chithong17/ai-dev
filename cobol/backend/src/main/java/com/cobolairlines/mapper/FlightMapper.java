package com.cobolairlines.mapper;

import com.cobolairlines.dto.FlightDTO;
import com.cobolairlines.model.Flight;

public class FlightMapper {
    public static FlightDTO toDto(Flight f) {
        if (f == null) return null;
        FlightDTO d = new FlightDTO();
        d.setFlightId(f.getFlightId());
        d.setFlightNum(f.getFlightNum());
        d.setFlightDate(f.getFlightDate());
        d.setAirportDep(f.getAirportDep());
        d.setAirportArr(f.getAirportArr());
        d.setDepTime(f.getDepTime());
        d.setArrTime(f.getArrTime());
        return d;
    }
}
