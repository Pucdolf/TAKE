package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Airplane;
import pl.polsl.take.airline.repositories.AirplaneRepository;

@RestController
@RequestMapping("/airplanes")
public class AirplaneController {

    @Autowired
    private AirplaneRepository airplaneRepository;

    @GetMapping
    public Iterable<Airplane> getAllAirplanes() {
        return airplaneRepository.findAll();
    }

    @PostMapping
    public Airplane addAirplane(@RequestBody Airplane airplane) {
        return airplaneRepository.save(airplane);
    }
}
