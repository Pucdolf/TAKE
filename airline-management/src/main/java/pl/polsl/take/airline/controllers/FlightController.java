package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Flight;
import pl.polsl.take.airline.repositories.FlightRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping
    public CollectionModel<EntityModel<Flight>> getAllFlights() {
        List<EntityModel<Flight>> flights = StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                .map(flight -> EntityModel.of(flight,
                        linkTo(methodOn(FlightController.class).getFlightById(flight.getId())).withSelfRel(),
                        linkTo(methodOn(FlightController.class).getAllFlights()).withRel("flights")))
                .collect(Collectors.toList());

        return CollectionModel.of(flights, linkTo(methodOn(FlightController.class).getAllFlights()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Flight> getFlightById(@PathVariable Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono lotu o ID: " + id));

        return EntityModel.of(flight,
                linkTo(methodOn(FlightController.class).getFlightById(id)).withSelfRel(),
                linkTo(methodOn(FlightController.class).getAllFlights()).withRel("all-flights"));
    }

    @PostMapping
    public Flight addFlight(@RequestBody Flight flight) {
        return flightRepository.save(flight);
    }

    @PutMapping("/{id}")
    public Flight updateFlight(@PathVariable Long id, @RequestBody Flight updatedFlight) {
        return flightRepository.findById(id).map(flight -> {
            flight.setFlightNumber(updatedFlight.getFlightNumber());
            flight.setOriginAirport(updatedFlight.getOriginAirport());
            flight.setDestinationAirport(updatedFlight.getDestinationAirport());
            flight.setAirplane(updatedFlight.getAirplane());
            flight.setStatus(updatedFlight.getStatus());
            flight.setPilot(updatedFlight.getPilot());
            flight.setDepartureTime(updatedFlight.getDepartureTime());
            flight.setArrivalTime(updatedFlight.getArrivalTime());
            return flightRepository.save(flight);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono lotu o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteFlight(@PathVariable Long id) {
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Nie można usunąć. Lot o ID: " + id + " nie istnieje.");
        }
        flightRepository.deleteById(id);
    }

    @GetMapping("/reports/finance")
    public List<pl.polsl.take.airline.repositories.FlightFinanceReport> getFinanceReport() {
        return flightRepository.getFinanceReport();
    }

    @GetMapping("/reports/load-factor")
    public List<pl.polsl.take.airline.repositories.FlightLoadFactorReport> getHighLoadFlights() {
        return flightRepository.getHighLoadFlights();
    }

    @PutMapping("/cancel-from/{iataCode}")
    public void cancelFlightsFromAirport(@PathVariable String iataCode) {
        flightRepository.cancelFlightsFromAirport(iataCode);
    }

    @PutMapping("/swap-airplane")
    public void swapAirplane(@RequestParam Long oldId, @RequestParam Long newId) {
        flightRepository.swapAirplane(oldId, newId);
    }
}