package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Passenger;
import pl.polsl.take.airline.repositories.PassengerRepository;
import pl.polsl.take.airline.dto.PassengerDTO;
import pl.polsl.take.airline.dto.PassengerRequestDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/passengers")
public class PassengerController {
    @Autowired private PassengerRepository repository;

    public PassengerDTO convertToDto(Passenger entity) {
        PassengerDTO dto = new PassengerDTO(entity);
        dto.add(linkTo(methodOn(PassengerController.class).getPassengerById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(PassengerController.class).getAllPassengers()).withRel("passengers"));
        return dto;
    }

    @GetMapping
    public CollectionModel<PassengerDTO> getAllPassengers() {
        List<PassengerDTO> dtos = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDto).collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(PassengerController.class).getAllPassengers()).withSelfRel());
    }

    @GetMapping("/{id}")
    public PassengerDTO getPassengerById(@PathVariable Long id) {
        return convertToDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Brak ID: " + id)));
    }

    @PostMapping
    public PassengerDTO addPassenger(@RequestBody PassengerRequestDTO req) {
        Passenger entity = new Passenger();
        entity.setFirstName(req.getFirstName());
        entity.setLastName(req.getLastName());
        entity.setEmail(req.getEmail());
        entity.setPassportNumber(req.getPassportNumber());
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public PassengerDTO updatePassenger(@PathVariable Long id, @RequestBody PassengerRequestDTO req) {
        return repository.findById(id).map(entity -> {
            entity.setFirstName(req.getFirstName());
            entity.setLastName(req.getLastName());
            entity.setEmail(req.getEmail());
            entity.setPassportNumber(req.getPassportNumber());
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Brak ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletePassenger(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brak ID: " + id);
        repository.deleteById(id);
    }

    @GetMapping("/vips")
    public Iterable<pl.polsl.take.airline.repositories.VipPassengerReport> getVipPassengers() {
        return repository.findVipPassengers();
    }
}
