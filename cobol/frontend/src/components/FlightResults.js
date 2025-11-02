import React from 'react';

export default function FlightResults({rows}){
  if(!rows || rows.length===0) return <div>No results</div>;
  return (
    <table style={{width:'100%',marginTop:12}}>
      <thead>
        <tr>
          <th>Flight</th><th>Date</th><th>Dep</th><th>Arr</th><th>DepTime</th>
        </tr>
      </thead>
      <tbody>
        {rows.map(r=> (
          <tr key={r.flightId}>
            <td>{r.flightNum}</td>
            <td>{r.flightDate}</td>
            <td>{r.airportDep}</td>
            <td>{r.airportArr}</td>
            <td>{r.depTime}</td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}
