import React, { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import api from "../lib/api";

export default function BookingSummary() {
  const [tickets, setTickets] = useState([]);
  const [searchParams] = useSearchParams();
  const clientId = searchParams.get("clientId");

  useEffect(() => {
    const fetchTickets = async () => {
      try {
        const resp = await api.get("/api/bookings/summary", {
          params: { clientId },
        });
        setTickets(resp.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchTickets();
  }, [clientId]);

  return (
    <div className="terminal-page">
      <h2>Booking Summary</h2>
      {tickets.length === 0 ? (
        <p>No tickets found for {clientId}</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Ticket ID</th>
              <th>Client</th>
              <th>Flight</th>
              <th>Passenger Seq</th>
              <th>Total Price</th>
              <th>Created At</th>
            </tr>
          </thead>
          <tbody>
            {tickets.map((t) => (
              <tr key={t.ticketId}>
                <td>{t.ticketId}</td>
                <td>{t.clientId}</td>
                <td>{t.flightId}</td>
                <td>{t.passengerSeq}</td>
                <td>{t.totalPrice}</td>
                <td>{t.createdAt}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
