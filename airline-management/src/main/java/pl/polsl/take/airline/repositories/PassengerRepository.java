package pl.polsl.take.airline.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.airline.entities.Passenger;

import java.util.List;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger, Long> {

    @Query(value = "SELECT p.first_name AS firstName, p.last_name AS lastName, p.passport_number AS passportNumber, " +
                   "COUNT(t.id) AS totalFlights, SUM(t.base_price) AS lifetimeValue " +
                   "FROM passengers p " +
                   "JOIN tickets t ON p.id = t.passenger_id " +
                   "GROUP BY p.id, p.first_name, p.last_name, p.passport_number " +
                   "HAVING COUNT(t.id) >= 3 " +
                   "ORDER BY lifetimeValue DESC", nativeQuery = true)
    List<VipPassengerReport> findVipPassengers();
}