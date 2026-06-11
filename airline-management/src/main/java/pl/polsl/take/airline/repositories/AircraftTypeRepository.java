package pl.polsl.take.airline.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.airline.entities.AircraftType;

@Repository
public interface AircraftTypeRepository extends CrudRepository<AircraftType, Long> {
}
