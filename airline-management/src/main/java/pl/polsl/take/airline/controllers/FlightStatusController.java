package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.FlightStatus;
import pl.polsl.take.airline.repositories.FlightStatusRepository;

@RestController
@RequestMapping("/flight-statuses")
public class FlightStatusController {

    @Autowired
    private FlightStatusRepository flightStatusRepository;

    @GetMapping
    public Iterable<FlightStatus> getAllStatuses() {
        return flightStatusRepository.findAll();
    }

    @PostMapping
    public FlightStatus addStatus(@RequestBody FlightStatus status) {
        return flightStatusRepository.save(status);
    }
}
