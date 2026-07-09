package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Flight;
import pl.polsl.take.airline.repositories.FlightRepository;
import pl.polsl.take.airline.repositories.AirportRepository;
import pl.polsl.take.airline.repositories.AirplaneRepository;
import pl.polsl.take.airline.repositories.FlightStatusRepository;
import pl.polsl.take.airline.dto.FlightDTO;
import pl.polsl.take.airline.dto.FlightRequestDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/flights")
public class FlightController {
    @Autowired private FlightRepository repository;
    @Autowired private AirportRepository airportRepository;
    @Autowired private AirplaneRepository airplaneRepository;
    @Autowired private FlightStatusRepository flightStatusRepository;

    public FlightDTO convertToDto(Flight entity) {
        FlightDTO dto = new FlightDTO(entity);
        dto.add(linkTo(methodOn(FlightController.class).getFlightById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(FlightController.class).getAllFlights()).withRel("flights"));
        if (entity.getOriginAirport() != null) dto.add(linkTo(methodOn(AirportController.class).getAirportById(entity.getOriginAirport().getId())).withRel("originAirport"));
        if (entity.getDestinationAirport() != null) dto.add(linkTo(methodOn(AirportController.class).getAirportById(entity.getDestinationAirport().getId())).withRel("destinationAirport"));
        if (entity.getAirplane() != null) dto.add(linkTo(methodOn(AirplaneController.class).getAirplaneById(entity.getAirplane().getId())).withRel("airplane"));
        if (entity.getStatus() != null) dto.add(linkTo(methodOn(FlightStatusController.class).getFlightStatusById(entity.getStatus().getId())).withRel("status"));
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
    public FlightDTO addFlight(@RequestBody FlightRequestDTO req) {
        Flight entity = new Flight();
        entity.setFlightNumber(req.getFlightNumber());
        entity.setDepartureTime(req.getDepartureTime());
        entity.setArrivalTime(req.getArrivalTime());
        
        if (req.getOriginAirportId() != null) entity.setOriginAirport(airportRepository.findById(req.getOriginAirportId()).orElseThrow(() -> new RuntimeException("Brak Origin Airport ID")));
        if (req.getDestinationAirportId() != null) entity.setDestinationAirport(airportRepository.findById(req.getDestinationAirportId()).orElseThrow(() -> new RuntimeException("Brak Destination Airport ID")));
        if (req.getAirplaneId() != null) entity.setAirplane(airplaneRepository.findById(req.getAirplaneId()).orElseThrow(() -> new RuntimeException("Brak Airplane ID")));
        if (req.getStatusId() != null) entity.setStatus(flightStatusRepository.findById(req.getStatusId()).orElseThrow(() -> new RuntimeException("Brak Status ID")));
        
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public FlightDTO updateFlight(@PathVariable Long id, @RequestBody FlightRequestDTO req) {
        return repository.findById(id).map(entity -> {
            entity.setFlightNumber(req.getFlightNumber());
            entity.setDepartureTime(req.getDepartureTime());
            entity.setArrivalTime(req.getArrivalTime());
            
            if (req.getOriginAirportId() != null) entity.setOriginAirport(airportRepository.findById(req.getOriginAirportId()).orElseThrow(() -> new RuntimeException("Brak Origin Airport ID")));
            if (req.getDestinationAirportId() != null) entity.setDestinationAirport(airportRepository.findById(req.getDestinationAirportId()).orElseThrow(() -> new RuntimeException("Brak Destination Airport ID")));
            if (req.getAirplaneId() != null) entity.setAirplane(airplaneRepository.findById(req.getAirplaneId()).orElseThrow(() -> new RuntimeException("Brak Airplane ID")));
            if (req.getStatusId() != null) entity.setStatus(flightStatusRepository.findById(req.getStatusId()).orElseThrow(() -> new RuntimeException("Brak Status ID")));
            
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Brak ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteFlight(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brak ID: " + id);
        repository.deleteById(id);
    }

    @GetMapping("/financial-reports")
    public Iterable<pl.polsl.take.airline.repositories.FlightFinanceReport> getFinanceReport() {
        return repository.getFinanceReport();
    }

    @GetMapping("/occupancy")
    public Iterable<pl.polsl.take.airline.repositories.FlightLoadFactorReport> getHighLoadFlights() {
        return repository.getHighLoadFlights();
    }

    @PutMapping("/cancel-by-airport/{airportCode}")
    public void cancelFlightsFromAirport(@PathVariable String airportCode) {
        repository.cancelFlightsFromAirport(airportCode);
    }

    @PutMapping("/replace-airplane")
    public void swapAirplane(@RequestParam Long oldAirplaneId, @RequestParam Long newAirplaneId) {
        repository.swapAirplane(oldAirplaneId, newAirplaneId);
    }
}
