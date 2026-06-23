package pl.polsl.take.airline.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.airline.entities.Flight;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {

    //Pełny raport finansowy dla każdego lotu
    @Query(value = "SELECT f.flight_number AS flightNumber, " +
                   "origin.iata_code AS originAirport, " +
                   "dest.iata_code AS destinationAirport, " +
                   "COUNT(t.id) AS ticketsSold, " +
                   "SUM(t.base_price) AS totalRevenue " +
                   "FROM flights f " +
                   "JOIN airports origin ON f.origin_airport_id = origin.id " +
                   "JOIN airports dest ON f.destination_airport_id = dest.id " +
                   "LEFT JOIN tickets t ON f.id = t.flight_id " +
                   "GROUP BY f.flight_number, origin.iata_code, dest.iata_code " +
                   "ORDER BY totalRevenue DESC", nativeQuery = true)
    List<FlightFinanceReport> getFinanceReport();

    //Analiza obłożenia samolotów powyżej 90%
    @Query(value = "SELECT f.flight_number AS flightNumber, " +
                   "a.model AS airplaneModel, " +
                   "a.capacity AS totalSeats, " +
                   "COUNT(t.id) AS seatsTaken, " +
                   "(COUNT(t.id) * 100.0 / a.capacity) AS loadFactorPercentage " +
                   "FROM flights f " +
                   "JOIN airplanes a ON f.airplane_id = a.id " +
                   "LEFT JOIN tickets t ON f.id = t.flight_id " +
                   "GROUP BY f.flight_number, a.model, a.capacity " +
                   "HAVING (COUNT(t.id) * 100.0 / a.capacity) > 90 " +
                   "ORDER BY loadFactorPercentage DESC", nativeQuery = true)
    List<FlightLoadFactorReport> getHighLoadFlights();

    //Masowe odwołanie lotów z konkretnego lotniska
    @Modifying
    @Transactional
    @Query(value = "UPDATE flights " +
                   "SET status_id = (SELECT id FROM flight_statuses WHERE code = 'CANCELLED') " +
                   "WHERE origin_airport_id = (SELECT id FROM airports WHERE iata_code = :iataCode) " +
                   "AND departure_time > CURRENT_TIMESTAMP", nativeQuery = true)
    void cancelFlightsFromAirport(String iataCode);

    //Uziemienie samolotu i podmiana maszyny
    @Modifying
    @Transactional
    @Query(value = "UPDATE flights " +
                   "SET airplane_id = :newAirplaneId " +
                   "WHERE airplane_id = :oldAirplaneId " +
                   "AND departure_time > CURRENT_TIMESTAMP", nativeQuery = true)
    void swapAirplane(Long oldAirplaneId, Long newAirplaneId);
}
