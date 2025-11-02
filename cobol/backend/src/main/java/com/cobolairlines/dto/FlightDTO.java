package com.cobolairlines.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightDTO {
    private Long flightId;
    private String flightNum;
    private LocalDate flightDate;
    private String airportDep;
    private String airportArr;
    private LocalTime depTime;
    private LocalTime arrTime;

    // getters/setters
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public String getFlightNum() { return flightNum; }
    public void setFlightNum(String flightNum) { this.flightNum = flightNum; }
    public LocalDate getFlightDate() { return flightDate; }
    public void setFlightDate(LocalDate flightDate) { this.flightDate = flightDate; }
    public String getAirportDep() { return airportDep; }
    public void setAirportDep(String airportDep) { this.airportDep = airportDep; }
    public String getAirportArr() { return airportArr; }
    public void setAirportArr(String airportArr) { this.airportArr = airportArr; }
    public LocalTime getDepTime() { return depTime; }
    public void setDepTime(LocalTime depTime) { this.depTime = depTime; }
    public LocalTime getArrTime() { return arrTime; }
    public void setArrTime(LocalTime arrTime) { this.arrTime = arrTime; }
}
