package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Airport;
import pl.polsl.take.airline.repositories.AirportRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/airports")
public class AirportController {

    @Autowired
    private AirportRepository airportRepository;

    @GetMapping
    public CollectionModel<EntityModel<Airport>> getAllAirports() {
        List<EntityModel<Airport>> airports = StreamSupport.stream(airportRepository.findAll().spliterator(), false)
                .map(airport -> EntityModel.of(airport,
                        linkTo(methodOn(AirportController.class).getAirportById(airport.getId())).withSelfRel(),
                        linkTo(methodOn(AirportController.class).getAllAirports()).withRel("airports")))
                .collect(Collectors.toList());

        return CollectionModel.of(airports, linkTo(methodOn(AirportController.class).getAllAirports()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Airport> getAirportById(@PathVariable Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono lotniska o ID: " + id));

        return EntityModel.of(airport,
                linkTo(methodOn(AirportController.class).getAirportById(id)).withSelfRel(),
                linkTo(methodOn(AirportController.class).getAllAirports()).withRel("all-airports"));
    }

    @PostMapping
    public Airport addAirport(@RequestBody Airport airport) {
        return airportRepository.save(airport);
    }

    @PutMapping("/{id}")
    public Airport updateAirport(@PathVariable Long id, @RequestBody Airport updatedAirport) {
        return airportRepository.findById(id).map(airport -> {
            airport.setIataCode(updatedAirport.getIataCode());
            airport.setName(updatedAirport.getName());
            airport.setCity(updatedAirport.getCity());
            airport.setCountry(updatedAirport.getCountry());
            return airportRepository.save(airport);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono lotniska o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteAirport(@PathVariable Long id) {
        if (!airportRepository.existsById(id)) {
            throw new RuntimeException("Nie można usunąć. Lotnisko o ID: " + id + " nie istnieje.");
        }
        airportRepository.deleteById(id);
    }
}