# Data Model: Airline Management System

**Feature Branch**: `1-airline-management`  
**Created**: 2025-11-02  

## Entities and Relationships

### User
- **Fields**:
  - `id` (UUID, primary key)
  - `username` (String, unique, required)
  - `password` (String, required)
  - `role` (Enum: CUSTOMER, ADMIN, required)
- **Relationships**:
  - None

### Flight
- **Fields**:
  - `id` (UUID, primary key)
  - `origin` (String, required)
  - `destination` (String, required)
  - `departure_time` (Timestamp, required)
  - `arrival_time` (Timestamp, required)
  - `capacity` (Integer, required)
  - `available_seats` (Integer, required)
- **Relationships**:
  - None

### Booking
- **Fields**:
  - `id` (UUID, primary key)
  - `flight_id` (UUID, foreign key to Flight, required)
  - `customer_id` (UUID, foreign key to Customer, required)
  - `seat_number` (String, required)
  - `booking_time` (Timestamp, required)
- **Relationships**:
  - Many-to-One with Flight
  - Many-to-One with Customer

### Customer
- **Fields**:
  - `id` (UUID, primary key)
  - `name` (String, required)
  - `email` (String, unique, required)
  - `phone` (String, required)
- **Relationships**:
  - One-to-Many with Booking

### Revenue Report
- **Fields**:
  - `id` (UUID, primary key)
  - `generated_at` (Timestamp, required)
  - `total_revenue` (Decimal, required)
- **Relationships**:
  - None

## Validation Rules

- **User**:
  - `username` must be unique.
  - `password` must meet security standards (e.g., minimum length, complexity).
- **Flight**:
  - `departure_time` must be before `arrival_time`.
  - `capacity` must be greater than 0.
- **Booking**:
  - `seat_number` must be unique per flight.
  - `available_seats` in Flight must be decremented on booking.
- **Customer**:
  - `email` must be unique and valid.

## Notes

- This data model is subject to review and updates as the project progresses.