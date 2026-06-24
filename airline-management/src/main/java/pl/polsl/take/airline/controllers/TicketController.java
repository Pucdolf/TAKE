package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Ticket;
import pl.polsl.take.airline.repositories.TicketRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public CollectionModel<EntityModel<Ticket>> getAllTickets() {
        List<EntityModel<Ticket>> tickets = StreamSupport.stream(ticketRepository.findAll().spliterator(), false)
                .map(ticket -> EntityModel.of(ticket,
                        linkTo(methodOn(TicketController.class).getTicketById(ticket.getId())).withSelfRel(),
                        linkTo(methodOn(TicketController.class).getAllTickets()).withRel("tickets")))
                .collect(Collectors.toList());

        return CollectionModel.of(tickets, linkTo(methodOn(TicketController.class).getAllTickets()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Ticket> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono biletu o ID: " + id));

        return EntityModel.of(ticket,
                linkTo(methodOn(TicketController.class).getTicketById(id)).withSelfRel(),
                linkTo(methodOn(TicketController.class).getAllTickets()).withRel("all-tickets"));
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
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Nie można usunąć. Bilet o ID: " + id + " nie istnieje.");
        }
        ticketRepository.deleteById(id);
    }
    
    @PutMapping("/flight/{flightId}/dynamic-pricing")
    public void applyDynamicPricing(@PathVariable Long flightId) {
        ticketRepository.updateTicketPricesForFlight(flightId);
    }
}