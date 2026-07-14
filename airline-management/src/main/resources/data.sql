-- Kraje
INSERT INTO countries (iso_code, name) VALUES ('PL', 'Poland');
INSERT INTO countries (iso_code, name) VALUES ('DE', 'Germany');
INSERT INTO countries (iso_code, name) VALUES ('UK', 'United Kingdom');
INSERT INTO countries (iso_code, name) VALUES ('IT', 'Italy');

-- Statusy lotów
INSERT INTO flight_statuses (code, description) VALUES ('SCH', 'Scheduled');
INSERT INTO flight_statuses (code, description) VALUES ('BOD', 'Boarding');
INSERT INTO flight_statuses (code, description) VALUES ('DEP', 'Departed');
INSERT INTO flight_statuses (code, description) VALUES ('ARR', 'Arrived');
INSERT INTO flight_statuses (code, description) VALUES ('CAN', 'Cancelled');

-- Typy statków powietrznych
INSERT INTO aircraft_types (name, description) VALUES ('Boeing 737-800', 'Narrow-body airliner');
INSERT INTO aircraft_types (name, description) VALUES ('Airbus A320neo', 'Narrow-body highly efficient airliner');
INSERT INTO aircraft_types (name, description) VALUES ('Cessna 208', 'Small regional turboprop');
INSERT INTO aircraft_types (name, description) VALUES ('Diamond DA40', 'Light 2-seat air taxi'); -- ID 4

-- Samoloty
INSERT INTO airplanes (model, registration_number, capacity, type_id) VALUES ('B738', 'SP-LWA', 189, 1);
INSERT INTO airplanes (model, registration_number, capacity, type_id) VALUES ('A320', 'D-AINA', 180, 2);
INSERT INTO airplanes (model, registration_number, capacity, type_id) VALUES ('C208', 'SP-VIP', 4, 3);
INSERT INTO airplanes (model, registration_number, capacity, type_id) VALUES ('B738', 'SP-LWB', 189, 1); -- Zapasowy samolot (ID 4)
INSERT INTO airplanes (model, registration_number, capacity, type_id) VALUES ('DA40', 'SP-TWO', 2, 4); -- 2-miejscowy samolot (ID 5)

-- Lotniska
INSERT INTO airports (iata_code, name, city, country_id) VALUES ('WAW', 'Chopin Airport', 'Warsaw', 1);
INSERT INTO airports (iata_code, name, city, country_id) VALUES ('BER', 'Brandenburg Airport', 'Berlin', 2);
INSERT INTO airports (iata_code, name, city, country_id) VALUES ('LHR', 'Heathrow Airport', 'London', 3);
INSERT INTO airports (iata_code, name, city, country_id) VALUES ('FCO', 'Fiumicino Airport', 'Rome', 4);

-- Pracownicy
INSERT INTO employees (first_name, last_name, email, job_title, license_number) VALUES ('Jan', 'Kowalski', 'jan.kowalski@airline.com', 'Pilot', 'LIC-123456');
INSERT INTO employees (first_name, last_name, email, job_title, license_number) VALUES ('Anna', 'Nowak', 'anna.nowak@airline.com', 'Flight Attendant', NULL);

-- Loty
-- Lot 1 (ID 1): Z WAW do BER (Standardowy lot testowy)
INSERT INTO flights (flight_number, origin_airport_id, destination_airport_id, airplane_id, status_id, departure_time, arrival_time) VALUES ('LO385', 1, 2, 1, 1, '2026-12-01 10:00:00', '2026-12-01 11:30:00');

-- Lot 2 (ID 2): Z WAW do LHR (Do testu masowego odwoływania)
INSERT INTO flights (flight_number, origin_airport_id, destination_airport_id, airplane_id, status_id, departure_time, arrival_time) VALUES ('LO281', 1, 3, 2, 1, '2026-12-01 12:00:00', '2026-12-01 14:00:00');

-- Lot 3 (ID 3): Z BER do FCO (W 4 miejscowej Cessnie)
INSERT INTO flights (flight_number, origin_airport_id, destination_airport_id, airplane_id, status_id, departure_time, arrival_time) VALUES ('VIP01', 2, 4, 3, 1, '2026-12-02 08:00:00', '2026-12-02 10:00:00');

-- Lot 4 (ID 4): Z LHR do FCO (W ultralekkim 2-miejscowym samolocie Diamond DA40)
INSERT INTO flights (flight_number, origin_airport_id, destination_airport_id, airplane_id, status_id, departure_time, arrival_time) VALUES ('TAXI2', 3, 4, 5, 1, '2026-12-05 16:00:00', '2026-12-05 18:30:00');

-- Pasażerowie
INSERT INTO passengers (first_name, last_name, email, passport_number) VALUES ('Michal', 'Zieliński', 'michal.z@example.com', 'PASS12345');
INSERT INTO passengers (first_name, last_name, email, passport_number) VALUES ('Julia', 'Wójcik', 'julia.w@example.com', 'PASS67890');
INSERT INTO passengers (first_name, last_name, email, passport_number) VALUES ('Janusz', 'Biznesu', 'vip@example.com', 'PASS99999'); -- Pasażer VIP
INSERT INTO passengers (first_name, last_name, email, passport_number) VALUES ('Anna', 'Kowalska', 'anna.k@example.com', 'PASS11111');

-- Bilety
-- Bilety na Lot 1 (LO385)
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (1, 1, 'Economy', '12A', 450.00, '2026-06-20 12:00:00');
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (1, 2, 'Business', '2C', 1200.00, '2026-06-21 15:30:00');

-- Bilet dla VIP-a na Loty 1 i 2
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (1, 3, 'Business', '1A', 1500.00, '2026-06-01 10:00:00');
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (2, 3, 'Business', '1A', 2000.00, '2026-06-02 10:00:00');

-- Bilety na Lot 3 (Cessna, obłożenie 100%)
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (3, 1, 'Economy', '1A', 5000.00, '2026-06-25 09:00:00');
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (3, 2, 'Economy', '1B', 5000.00, '2026-06-25 09:00:00');
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (3, 3, 'Business', '2A', 8000.00, '2026-06-25 09:00:00'); -- Trzeci bilet dla VIP-a
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (3, 4, 'Economy', '2B', 5000.00, '2026-06-25 09:00:00');

-- Bilety na NOWY Lot 4 (2-miejscowy samolot TAXI2, obłożenie 100%)
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (4, 1, 'Economy', '1', 1000.00, '2026-07-01 10:00:00');
INSERT INTO tickets (flight_id, passenger_id, seat_class, seat_number, base_price, booking_time) VALUES (4, 4, 'Business', '2', 2500.00, '2026-07-01 11:00:00');
