package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Airplane;
import pl.polsl.take.airline.repositories.AirplaneRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/airplanes")
public class AirplaneController {

    @Autowired
    private AirplaneRepository airplaneRepository;

    @GetMapping
    public CollectionModel<EntityModel<Airplane>> getAllAirplanes() {
        List<EntityModel<Airplane>> airplanes = StreamSupport.stream(airplaneRepository.findAll().spliterator(), false)
                .map(airplane -> EntityModel.of(airplane,
                        linkTo(methodOn(AirplaneController.class).getAirplaneById(airplane.getId())).withSelfRel(),
                        linkTo(methodOn(AirplaneController.class).getAllAirplanes()).withRel("airplanes")))
                .collect(Collectors.toList());

        return CollectionModel.of(airplanes, linkTo(methodOn(AirplaneController.class).getAllAirplanes()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Airplane> getAirplaneById(@PathVariable Long id) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono samolotu o ID: " + id));

        return EntityModel.of(airplane,
                linkTo(methodOn(AirplaneController.class).getAirplaneById(id)).withSelfRel(),
                linkTo(methodOn(AirplaneController.class).getAllAirplanes()).withRel("all-airplanes"));
    }

    @PostMapping
    public Airplane addAirplane(@RequestBody Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    @PutMapping("/{id}")
    public Airplane updateAirplane(@PathVariable Long id, @RequestBody Airplane updatedAirplane) {
        return airplaneRepository.findById(id).map(airplane -> {
            airplane.setModel(updatedAirplane.getModel());
            airplane.setRegistrationNumber(updatedAirplane.getRegistrationNumber());
            airplane.setCapacity(updatedAirplane.getCapacity());
            airplane.setType(updatedAirplane.getType());
            return airplaneRepository.save(airplane);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono samolotu o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteAirplane(@PathVariable Long id) {
        if (!airplaneRepository.existsById(id)) {
            throw new RuntimeException("Nie można usunąć. Samolot o ID: " + id + " nie istnieje.");
        }
        airplaneRepository.deleteById(id);
    }
}