package pl.polsl.take.airline.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.take.airline.entities.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {

    //Dynamiczna korekta cen biletów
    @Modifying
    @Transactional
    @Query(value = "UPDATE tickets " +
                   "SET base_price = CASE " +
                   "WHEN seat_class = 'Economy' THEN base_price * 0.90 " +
                   "WHEN seat_class = 'Business' THEN base_price * 1.15 " +
                   "ELSE base_price END " +
                   "WHERE flight_id = :flightId", nativeQuery = true)
    void updateTicketPricesForFlight(Long flightId);
}