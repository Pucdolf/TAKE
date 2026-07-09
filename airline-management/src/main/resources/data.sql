-- Kraje
INSERT INTO countries (iso_code, name) VALUES ('PL', 'Poland');
INSERT INTO countries (iso_code, name) VALUES ('DE', 'Germany');
INSERT INTO countries (iso_code, name) VALUES ('UK', 'United Kingdom');

-- Statusy lotów
INSERT INTO flight_statuses (code, description) VALUES ('SCH', 'Scheduled');
INSERT INTO flight_statuses (code, description) VALUES ('BOD', 'Boarding');
INSERT INTO flight_statuses (code, description) VALUES ('DEP', 'Departed');
INSERT INTO flight_statuses (code, description) VALUES ('ARR', 'Arrived');
INSERT INTO flight_statuses (code, description) VALUES ('CAN', 'Cancelled');

-- Typy statków powietrznych
INSERT INTO aircraft_types (name, description) VALUES ('Boeing 737-800', 'Narrow-body airliner');
INSERT INTO aircraft_types (name, description) VALUES ('Airbus A320neo', 'Narrow-body highly efficient airliner');

-- Samoloty
INSERT INTO airplanes (model, registration_number, capacity, type_id) VALUES ('B738', 'SP-LWA', 189, 1);
INSERT INTO airplanes (model, registration_number, capacity, type_id) VALUES ('A320', 'D-AINA', 180, 2);

-- Lotniska
INSERT INTO airports (iata_code, name, city, country_id) VALUES ('WAW', 'Chopin Airport', 'Warsaw', 1);
INSERT INTO airports (iata_code, name, city, country_id) VALUES ('BER', 'Brandenburg Airport', 'Berlin', 2);
INSERT INTO airports (iata_code, name, city, country_id) VALUES ('LHR', 'Heathrow Airport', 'London', 3);

-- Pracownicy
INSERT INTO employees (first_name, last_name, email, job_title, license_number) VALUES ('Jan', 'Kowalski', 'jan.kowalski@airline.com', 'Pilot', 'LIC-123456');
INSERT INTO employees (first_name, last_name, email, job_title, license_number) VALUES ('Anna', 'Nowak', 'anna.nowak@airline.com', 'Flight Attendant', NULL);

-- Loty
INSERT INTO flights (flight_number, origin_airport_id, destination_airport_id, airplane_id, status_id, departure_time, arrival_time) VALUES ('LO385', 1, 2, 1, 1, '2026-07-01 10:00:00', '2026-07-01 11:30:00');
INSERT INTO flights (flight_number, origin_airport_id, destination_airport_id, airplane_id, status_id, departure_time, arrival_time) VALUES ('LH22', 2, 3, 2, 1, '2026-07-01 14:00:00', '2026-07-01 15:15:00');

-- Pasażerowie
INSERT INTO passengers (first_name, last_name, email, passport_number) VALUES ('Michal', 'Zieliński', 'michal.z@example.com', 'PASS12345');
INSERT INTO passengers (first_name, last_name, email, passport_number) VALUES ('Julia', 'Wójcik', 'julia.w@example.com', 'PASS67890');

-- Bilety (Zauważ: baza relacyjna załatwia tu połączenie lotu i pasażera)
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (1, 1, 'ECONOMY', '12A', 450.00, '2026-06-20 12:00:00');
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (1, 2, 'BUSINESS', '2C', 1200.00, '2026-06-21 15:30:00');
