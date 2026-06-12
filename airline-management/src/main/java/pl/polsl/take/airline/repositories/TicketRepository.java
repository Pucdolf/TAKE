package pl.polsl.take.airline.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.airline.entities.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
