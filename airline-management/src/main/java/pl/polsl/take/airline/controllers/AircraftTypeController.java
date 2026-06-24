package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.AircraftType;
import pl.polsl.take.airline.repositories.AircraftTypeRepository;
import pl.polsl.take.airline.dto.AircraftTypeDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/aircraft-types")
public class AircraftTypeController {

    @Autowired
    private AircraftTypeRepository repository;

    private AircraftTypeDTO convertToDto(AircraftType entity) {
        AircraftTypeDTO dto = new AircraftTypeDTO(entity);
        dto.add(linkTo(methodOn(AircraftTypeController.class).getAircraftTypeById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(AircraftTypeController.class).getAllAircraftTypes()).withRel("aircraft-types"));
        return dto;
    }

    @GetMapping
    public CollectionModel<AircraftTypeDTO> getAllAircraftTypes() {
        List<AircraftTypeDTO> dtos = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDto).collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(AircraftTypeController.class).getAllAircraftTypes()).withSelfRel());
    }

    @GetMapping("/{id}")
    public AircraftTypeDTO getAircraftTypeById(@PathVariable Long id) {
        AircraftType entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono typu o ID: " + id));
        return convertToDto(entity);
    }

    @PostMapping
    public AircraftTypeDTO addAircraftType(@RequestBody AircraftType entity) {
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public AircraftTypeDTO updateAircraftType(@PathVariable Long id, @RequestBody AircraftType updated) {
        return repository.findById(id).map(entity -> {
            entity.setName(updated.getName());
            entity.setDescription(updated.getDescription());
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono typu o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteAircraftType(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Nie znaleziono: " + id);
        repository.deleteById(id);
    }
}
