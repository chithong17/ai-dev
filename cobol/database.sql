-- PostgreSQL schema for COBOL Airlines

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE flights (
    id SERIAL PRIMARY KEY,
    flight_number VARCHAR(20) UNIQUE NOT NULL,
    origin VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    flight_id INT REFERENCES flights(id),
    booking_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL
);

-- Seed data
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('user1', 'password1', 'CUSTOMER');

INSERT INTO flights (flight_number, origin, destination, departure_time, arrival_time, price) VALUES
('FL123', 'New York', 'Los Angeles', '2025-11-03 08:00:00', '2025-11-03 11:00:00', 299.99),
('FL456', 'Chicago', 'Miami', '2025-11-04 09:00:00', '2025-11-04 12:00:00', 199.99);