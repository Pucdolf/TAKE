package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.AircraftType;
import pl.polsl.take.airline.repositories.AircraftTypeRepository;
import pl.polsl.take.airline.dto.AircraftTypeDTO;
import pl.polsl.take.airline.dto.AircraftTypeRequestDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/aircraft-types")
public class AircraftTypeController {

    @Autowired private AircraftTypeRepository repository;

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
        return convertToDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Brak ID: " + id)));
    }

    @PostMapping
    public AircraftTypeDTO addAircraftType(@RequestBody AircraftTypeRequestDTO req) {
        AircraftType entity = new AircraftType();
        entity.setName(req.getName());
        entity.setDescription(req.getDescription());
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public AircraftTypeDTO updateAircraftType(@PathVariable Long id, @RequestBody AircraftTypeRequestDTO req) {
        return repository.findById(id).map(entity -> {
            entity.setName(req.getName());
            entity.setDescription(req.getDescription());
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Brak ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteAircraftType(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brak ID: " + id);
        repository.deleteById(id);
    }
}
