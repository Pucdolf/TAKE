package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Flight;
import pl.polsl.take.airline.repositories.FlightRepository;
import pl.polsl.take.airline.dto.FlightDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/flights")
public class FlightController {
    @Autowired private FlightRepository repository;

    public FlightDTO convertToDto(Flight entity) {
        FlightDTO dto = new FlightDTO(entity);
        dto.add(linkTo(methodOn(FlightController.class).getFlightById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(FlightController.class).getAllFlights()).withRel("flights"));
        if(entity.getOriginAirport() != null) dto.add(linkTo(methodOn(AirportController.class).getAirportById(entity.getOriginAirport().getId())).withRel("originAirport"));
        if(entity.getDestinationAirport() != null) dto.add(linkTo(methodOn(AirportController.class).getAirportById(entity.getDestinationAirport().getId())).withRel("destinationAirport"));
        if(entity.getAirplane() != null) dto.add(linkTo(methodOn(AirplaneController.class).getAirplaneById(entity.getAirplane().getId())).withRel("airplane"));
        if(entity.getStatus() != null) dto.add(linkTo(methodOn(FlightStatusController.class).getFlightStatusById(entity.getStatus().getId())).withRel("status"));
        return dto;
    }

    @GetMapping
    public CollectionModel<FlightDTO> getAllFlights() {
        List<FlightDTO> dtos = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDto).collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(FlightController.class).getAllFlights()).withSelfRel());
    }

    @GetMapping("/{id}")
    public FlightDTO getFlightById(@PathVariable Long id) {
        return convertToDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Brak ID: " + id)));
    }

    @PostMapping
    public FlightDTO addFlight(@RequestBody Flight entity) {
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public FlightDTO updateFlight(@PathVariable Long id, @RequestBody Flight updated) {
        return repository.findById(id).map(entity -> {
            entity.setFlightNumber(updated.getFlightNumber());
            entity.setOriginAirport(updated.getOriginAirport());
            entity.setDestinationAirport(updated.getDestinationAirport());
            entity.setAirplane(updated.getAirplane());
            entity.setStatus(updated.getStatus());
            entity.setDepartureTime(updated.getDepartureTime());
            entity.setArrivalTime(updated.getArrivalTime());
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Brak ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteFlight(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brak ID: " + id);
        repository.deleteById(id);
    }

    @GetMapping("/reports/finance")
    public List<pl.polsl.take.airline.repositories.FlightFinanceReport> getFinanceReport() {
        return repository.getFinanceReport();
    }

    @GetMapping("/reports/load-factor")
    public List<pl.polsl.take.airline.repositories.FlightLoadFactorReport> getHighLoadFlights() {
        return repository.getHighLoadFlights();
    }

    @PutMapping("/cancel-from/{iataCode}")
    public void cancelFlightsFromAirport(@PathVariable String iataCode) {
        repository.cancelFlightsFromAirport(iataCode);
    }

    @PutMapping("/swap-airplane")
    public void swapAirplane(@RequestParam Long oldId, @RequestParam Long newId) {
        repository.swapAirplane(oldId, newId);
    }
}
