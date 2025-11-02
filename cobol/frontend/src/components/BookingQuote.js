import React from 'react';

export default function BookingQuote({quote}){
  if(!quote) return null;
  return (
    <div style={{marginTop:12}}>
      <div>Client: {quote.clientFirstName} {quote.clientLastName} ({quote.clientId})</div>
      <div>Flight: {quote.flightNum} (ID {quote.flightId})</div>
      <div>Unit Price: {quote.unitPrice}</div>
      <div>Total Price: {quote.totalPrice}</div>
    </div>
  )
}
