package pl.polsl.take.airline.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.airline.entities.Passenger;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger, Long> {
}
