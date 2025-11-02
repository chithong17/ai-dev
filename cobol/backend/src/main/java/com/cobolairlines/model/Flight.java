package com.cobolairlines.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Long flightId;

    @Column(name = "flight_num")
    private String flightNum;

    @Column(name = "flight_date")
    private LocalDate flightDate;

    @Column(name = "airport_dep")
    private String airportDep;

    @Column(name = "airport_arr")
    private String airportArr;

    @Column(name = "dep_time")
    private LocalTime depTime;

    @Column(name = "arr_time")
    private LocalTime arrTime;

    // getters and setters
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
