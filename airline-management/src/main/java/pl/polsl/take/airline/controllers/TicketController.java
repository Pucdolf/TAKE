package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Ticket;
import pl.polsl.take.airline.repositories.TicketRepository;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public Iterable<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @PostMapping
    public Ticket addTicket(@RequestBody Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable Long id, @RequestBody Ticket updatedTicket) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setFlight(updatedTicket.getFlight());
            ticket.setPassenger(updatedTicket.getPassenger());
            ticket.setSeatClass(updatedTicket.getSeatClass());
            ticket.setSeatNumber(updatedTicket.getSeatNumber());
            ticket.setBasePrice(updatedTicket.getBasePrice());
            return ticketRepository.save(ticket);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono biletu o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketRepository.deleteById(id);
    }

    @PutMapping("/flight/{flightId}/dynamic-pricing")
    public void applyDynamicPricing(@PathVariable Long flightId) {
        ticketRepository.updateTicketPricesForFlight(flightId);
    }
}
