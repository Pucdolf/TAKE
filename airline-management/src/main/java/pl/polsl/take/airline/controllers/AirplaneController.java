package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Airplane;
import pl.polsl.take.airline.repositories.AirplaneRepository;
import pl.polsl.take.airline.dto.AirplaneDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/airplanes")
public class AirplaneController {
    @Autowired private AirplaneRepository repository;

    public AirplaneDTO convertToDto(Airplane entity) {
        AirplaneDTO dto = new AirplaneDTO(entity);
        dto.add(linkTo(methodOn(AirplaneController.class).getAirplaneById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(AirplaneController.class).getAllAirplanes()).withRel("airplanes"));
        if (entity.getType() != null) {
             dto.add(linkTo(methodOn(AircraftTypeController.class).getAircraftTypeById(entity.getType().getId())).withRel("aircraftType"));
        }
        return dto;
    }

    @GetMapping
    public CollectionModel<AirplaneDTO> getAllAirplanes() {
        List<AirplaneDTO> dtos = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDto).collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(AirplaneController.class).getAllAirplanes()).withSelfRel());
    }

    @GetMapping("/{id}")
    public AirplaneDTO getAirplaneById(@PathVariable Long id) {
        return convertToDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Brak ID: " + id)));
    }

    @PostMapping
    public AirplaneDTO addAirplane(@RequestBody Airplane entity) {
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public AirplaneDTO updateAirplane(@PathVariable Long id, @RequestBody Airplane updated) {
        return repository.findById(id).map(entity -> {
            entity.setModel(updated.getModel());
            entity.setRegistrationNumber(updated.getRegistrationNumber());
            entity.setCapacity(updated.getCapacity());
            entity.setType(updated.getType());
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Brak ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteAirplane(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brak ID: " + id);
        repository.deleteById(id);
    }
}
