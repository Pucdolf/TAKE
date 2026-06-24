package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Passenger;
import pl.polsl.take.airline.repositories.PassengerRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    @Autowired
    private PassengerRepository passengerRepository;

    @GetMapping
    public CollectionModel<EntityModel<Passenger>> getAllPassengers() {
        List<EntityModel<Passenger>> passengers = StreamSupport.stream(passengerRepository.findAll().spliterator(), false)
                .map(passenger -> EntityModel.of(passenger,
                        linkTo(methodOn(PassengerController.class).getPassengerById(passenger.getId())).withSelfRel(),
                        linkTo(methodOn(PassengerController.class).getAllPassengers()).withRel("passengers")))
                .collect(Collectors.toList());

        return CollectionModel.of(passengers, linkTo(methodOn(PassengerController.class).getAllPassengers()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Passenger> getPassengerById(@PathVariable Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono pasażera o ID: " + id));

        return EntityModel.of(passenger,
                linkTo(methodOn(PassengerController.class).getPassengerById(id)).withSelfRel(),
                linkTo(methodOn(PassengerController.class).getAllPassengers()).withRel("all-passengers"));
    }

    @PostMapping
    public Passenger addPassenger(@RequestBody Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @PutMapping("/{id}")
    public Passenger updatePassenger(@PathVariable Long id, @RequestBody Passenger updatedPassenger) {
        return passengerRepository.findById(id).map(passenger -> {
            passenger.setFirstName(updatedPassenger.getFirstName());
            passenger.setLastName(updatedPassenger.getLastName());
            passenger.setEmail(updatedPassenger.getEmail());
            passenger.setPassportNumber(updatedPassenger.getPassportNumber());
            return passengerRepository.save(passenger);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono pasażera o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletePassenger(@PathVariable Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new RuntimeException("Nie można usunąć. Pasażer o ID: " + id + " nie istnieje.");
        }
        passengerRepository.deleteById(id);
    }

    @GetMapping("/vips")
    public Iterable<pl.polsl.take.airline.repositories.VipPassengerReport> getVipPassengers() {
        return passengerRepository.findVipPassengers();
    }
}