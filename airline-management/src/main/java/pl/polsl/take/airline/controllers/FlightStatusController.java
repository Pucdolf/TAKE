package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.FlightStatus;
import pl.polsl.take.airline.repositories.FlightStatusRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/flight-statuses")
public class FlightStatusController {

    @Autowired
    private FlightStatusRepository flightStatusRepository;

    @GetMapping
    public CollectionModel<EntityModel<FlightStatus>> getAllStatuses() {
        List<EntityModel<FlightStatus>> statuses = StreamSupport.stream(flightStatusRepository.findAll().spliterator(), false)
                .map(status -> EntityModel.of(status,
                        linkTo(methodOn(FlightStatusController.class).getStatusById(status.getId())).withSelfRel(),
                        linkTo(methodOn(FlightStatusController.class).getAllStatuses()).withRel("flight-statuses")))
                .collect(Collectors.toList());

        return CollectionModel.of(statuses, linkTo(methodOn(FlightStatusController.class).getAllStatuses()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<FlightStatus> getStatusById(@PathVariable Long id) {
        FlightStatus status = flightStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono statusu lotu o ID: " + id));

        return EntityModel.of(status,
                linkTo(methodOn(FlightStatusController.class).getStatusById(id)).withSelfRel(),
                linkTo(methodOn(FlightStatusController.class).getAllStatuses()).withRel("all-flight-statuses"));
    }

    @PostMapping
    public FlightStatus addStatus(@RequestBody FlightStatus status) {
        return flightStatusRepository.save(status);
    }

    @PutMapping("/{id}")
    public FlightStatus updateStatus(@PathVariable Long id, @RequestBody FlightStatus updatedStatus) {
        return flightStatusRepository.findById(id).map(status -> {
            status.setCode(updatedStatus.getCode());
            status.setName(updatedStatus.getName());
            status.setDescription(updatedStatus.getDescription());
            return flightStatusRepository.save(status);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono statusu lotu o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable Long id) {
        if (!flightStatusRepository.existsById(id)) {
            throw new RuntimeException("Nie można usunąć. Status lotu o ID: " + id + " nie istnieje.");
        }
        flightStatusRepository.deleteById(id);
    }
}