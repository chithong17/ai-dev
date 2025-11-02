import React from 'react';

export default function TerminalInput({label, name, value, onChange, type='text'}){
  return (
    <div className="terminal-input">
      <label>{label}</label>
      <input name={name} value={value} onChange={e=>onChange(e.target.value)} type={type} />
    </div>
  )
}
