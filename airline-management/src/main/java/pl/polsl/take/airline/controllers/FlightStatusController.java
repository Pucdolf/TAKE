package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.FlightStatus;
import pl.polsl.take.airline.repositories.FlightStatusRepository;
import pl.polsl.take.airline.dto.FlightStatusDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/flight-statuses")
public class FlightStatusController {
    @Autowired private FlightStatusRepository repository;

    public FlightStatusDTO convertToDto(FlightStatus entity) {
        FlightStatusDTO dto = new FlightStatusDTO(entity);
        dto.add(linkTo(methodOn(FlightStatusController.class).getFlightStatusById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(FlightStatusController.class).getAllFlightStatuses()).withRel("flight-statuses"));
        return dto;
    }

    @GetMapping
    public CollectionModel<FlightStatusDTO> getAllFlightStatuses() {
        List<FlightStatusDTO> dtos = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDto).collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(FlightStatusController.class).getAllFlightStatuses()).withSelfRel());
    }

    @GetMapping("/{id}")
    public FlightStatusDTO getFlightStatusById(@PathVariable Long id) {
        return convertToDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Brak ID: " + id)));
    }

    @PostMapping
    public FlightStatusDTO addFlightStatus(@RequestBody FlightStatus entity) {
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public FlightStatusDTO updateFlightStatus(@PathVariable Long id, @RequestBody FlightStatus updated) {
        return repository.findById(id).map(entity -> {
            entity.setCode(updated.getCode());
            entity.setDescription(updated.getDescription());
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Brak ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteFlightStatus(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brak ID: " + id);
        repository.deleteById(id);
    }
}
