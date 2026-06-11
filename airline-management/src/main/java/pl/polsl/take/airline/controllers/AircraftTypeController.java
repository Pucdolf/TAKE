package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.AircraftType;
import pl.polsl.take.airline.repositories.AircraftTypeRepository;

@RestController
@RequestMapping("/aircraft-types")
public class AircraftTypeController {

    @Autowired
    private AircraftTypeRepository aircraftTypeRepository;

    @GetMapping
    public Iterable<AircraftType> getAllTypes() {
        return aircraftTypeRepository.findAll();
    }

    @PostMapping
    public AircraftType addType(@RequestBody AircraftType type) {
        return aircraftTypeRepository.save(type);
    }
}
