package pl.polsl.take.airline.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.airline.entities.Flight;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {
}
