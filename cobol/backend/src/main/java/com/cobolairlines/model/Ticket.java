package com.cobolairlines.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "flight_id")
    private Long flightId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "passenger_seq")
    private Integer passengerSeq;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "created_by_empid")
    private String createdByEmpid;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    // getters and setters
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public Integer getPassengerSeq() { return passengerSeq; }
    public void setPassengerSeq(Integer passengerSeq) { this.passengerSeq = passengerSeq; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public String getCreatedByEmpid() { return createdByEmpid; }
    public void setCreatedByEmpid(String createdByEmpid) { this.createdByEmpid = createdByEmpid; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
