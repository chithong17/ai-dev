-- Suggested Postgres schema (bootstrap). Verify against DB2 artifacts and DCLGEN copybooks.

CREATE TABLE emplo (
  empid VARCHAR PRIMARY KEY,
  admidate DATE NOT NULL,
  deptid INT NOT NULL,
  stored_cryptpass VARCHAR NOT NULL
);

CREATE TABLE flight (
  flight_id BIGSERIAL PRIMARY KEY,
  flight_num VARCHAR NOT NULL,
  flight_date DATE NOT NULL,
  airport_dep VARCHAR(10),
  airport_arr VARCHAR(10),
  dep_time TIME,
  arr_time TIME,
  seats_total INT,
  seats_available INT
);

CREATE TABLE passeng (
  client_id VARCHAR PRIMARY KEY,
  first_name VARCHAR,
  last_name VARCHAR,
  dob DATE
);

CREATE TABLE ticket (
  ticket_id BIGSERIAL PRIMARY KEY,
  flight_id BIGINT REFERENCES flight(flight_id),
  client_id VARCHAR REFERENCES passeng(client_id),
  passenger_seq INT,
  unit_price NUMERIC(10,2),
  total_price NUMERIC(12,2),
  created_by_empid VARCHAR REFERENCES emplo(empid),
  created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_flight_num_date ON flight(flight_num, flight_date);
CREATE INDEX idx_flight_date ON flight(flight_date);
CREATE INDEX idx_passeng_clientid ON passeng(client_id);

-- TODO: add other tables (ticket details, airport, department) as required by DB2 create-db
