package pl.polsl.take.airline.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Flight;
import pl.polsl.take.airline.repositories.FlightRepository;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping
    public Iterable<Flight> getAllFlights() {
        return flightRepository.findAll();
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
