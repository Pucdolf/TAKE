package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Ticket;
import pl.polsl.take.airline.repositories.TicketRepository;
import pl.polsl.take.airline.repositories.FlightRepository;
import pl.polsl.take.airline.repositories.PassengerRepository;
import pl.polsl.take.airline.dto.TicketDTO;
import pl.polsl.take.airline.dto.TicketRequestDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired private TicketRepository repository;
    @Autowired private FlightRepository flightRepository;
    @Autowired private PassengerRepository passengerRepository;

    public TicketDTO convertToDto(Ticket entity) {
        TicketDTO dto = new TicketDTO(entity);
        dto.add(linkTo(methodOn(TicketController.class).getTicketById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(TicketController.class).getAllTickets()).withRel("tickets"));
        if (entity.getFlight() != null) dto.add(linkTo(methodOn(FlightController.class).getFlightById(entity.getFlight().getId())).withRel("flight"));
        if (entity.getPassenger() != null) dto.add(linkTo(methodOn(PassengerController.class).getPassengerById(entity.getPassenger().getId())).withRel("passenger"));
        return dto;
    }

    @GetMapping
    public CollectionModel<TicketDTO> getAllTickets() {
        List<TicketDTO> dtos = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDto).collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(TicketController.class).getAllTickets()).withSelfRel());
    }

    @GetMapping("/{id}")
    public TicketDTO getTicketById(@PathVariable Long id) {
        return convertToDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Brak ID: " + id)));
    }

    @PostMapping
    public TicketDTO addTicket(@RequestBody TicketRequestDTO req) {
        Ticket entity = new Ticket();
        entity.setSeatClass(req.getSeatClass());
        entity.setSeatNumber(req.getSeatNumber());
        entity.setBasePrice(req.getBasePrice());
        entity.setBookingTime(req.getBookingTime());
        
        if (req.getFlightId() != null) entity.setFlight(flightRepository.findById(req.getFlightId()).orElseThrow(() -> new RuntimeException("Brak Flight ID")));
        if (req.getPassengerId() != null) entity.setPassenger(passengerRepository.findById(req.getPassengerId()).orElseThrow(() -> new RuntimeException("Brak Passenger ID")));
        
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public TicketDTO updateTicket(@PathVariable Long id, @RequestBody TicketRequestDTO req) {
        return repository.findById(id).map(entity -> {
            entity.setSeatClass(req.getSeatClass());
            entity.setSeatNumber(req.getSeatNumber());
            entity.setBasePrice(req.getBasePrice());
            entity.setBookingTime(req.getBookingTime());
            
            if (req.getFlightId() != null) entity.setFlight(flightRepository.findById(req.getFlightId()).orElseThrow(() -> new RuntimeException("Brak Flight ID")));
            if (req.getPassengerId() != null) entity.setPassenger(passengerRepository.findById(req.getPassengerId()).orElseThrow(() -> new RuntimeException("Brak Passenger ID")));
            
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Brak ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brak ID: " + id);
        repository.deleteById(id);
    }

    @PutMapping("/flight/{flightId}/dynamic-pricing")
    public void updateTicketPricesForFlight(@PathVariable Long flightId) {
        repository.updateTicketPricesForFlight(flightId);
    }
}
