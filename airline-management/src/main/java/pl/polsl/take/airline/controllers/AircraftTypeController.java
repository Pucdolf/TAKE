package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.AircraftType;
import pl.polsl.take.airline.repositories.AircraftTypeRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/aircraft-types")
public class AircraftTypeController {

    @Autowired
    private AircraftTypeRepository aircraftTypeRepository;

    @GetMapping
    public CollectionModel<EntityModel<AircraftType>> getAllTypes() {
        List<EntityModel<AircraftType>> types = StreamSupport.stream(aircraftTypeRepository.findAll().spliterator(), false)
                .map(type -> EntityModel.of(type,
                        linkTo(methodOn(AircraftTypeController.class).getTypeById(type.getId())).withSelfRel(),
                        linkTo(methodOn(AircraftTypeController.class).getAllTypes()).withRel("aircraft-types")))
                .collect(Collectors.toList());

        return CollectionModel.of(types, linkTo(methodOn(AircraftTypeController.class).getAllTypes()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<AircraftType> getTypeById(@PathVariable Long id) {
        AircraftType type = aircraftTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono typu samolotu o ID: " + id));

        return EntityModel.of(type,
                linkTo(methodOn(AircraftTypeController.class).getTypeById(id)).withSelfRel(),
                linkTo(methodOn(AircraftTypeController.class).getAllTypes()).withRel("all-aircraft-types"));
    }

    @PostMapping
    public AircraftType addType(@RequestBody AircraftType type) {
        return aircraftTypeRepository.save(type);
    }

    @PutMapping("/{id}")
    public AircraftType updateType(@PathVariable Long id, @RequestBody AircraftType updatedType) {
        return aircraftTypeRepository.findById(id).map(type -> {
            type.setName(updatedType.getName());
            type.setDescription(updatedType.getDescription());
            return aircraftTypeRepository.save(type);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono typu samolotu o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteType(@PathVariable Long id) {
        if (!aircraftTypeRepository.existsById(id)) {
            throw new RuntimeException("Nie można usunąć. Typ samolotu o ID: " + id + " nie istnieje.");
        }
        aircraftTypeRepository.deleteById(id);
    }
}