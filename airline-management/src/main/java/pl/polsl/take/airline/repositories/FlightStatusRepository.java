package pl.polsl.take.airline.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.airline.entities.FlightStatus;

@Repository
public interface FlightStatusRepository extends CrudRepository<FlightStatus, Long> {
}
