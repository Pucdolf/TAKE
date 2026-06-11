package pl.polsl.take.airline.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.airline.entities.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
}
