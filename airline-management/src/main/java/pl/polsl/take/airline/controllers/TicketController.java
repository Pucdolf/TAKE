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
}
