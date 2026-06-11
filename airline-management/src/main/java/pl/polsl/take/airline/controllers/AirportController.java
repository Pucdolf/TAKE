package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Airport;
import pl.polsl.take.airline.repositories.AirportRepository;

@RestController
@RequestMapping("/airports")
public class AirportController {

    @Autowired
    private AirportRepository airportRepository;

    @GetMapping
    public Iterable<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    @PostMapping
    public Airport addAirport(@RequestBody Airport airport) {
        return airportRepository.save(airport);
    }
}
