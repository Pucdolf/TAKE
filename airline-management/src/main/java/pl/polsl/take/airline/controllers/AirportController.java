package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Airport;
import pl.polsl.take.airline.repositories.AirportRepository;
import pl.polsl.take.airline.dto.AirportDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/airports")
public class AirportController {
    @Autowired private AirportRepository repository;

    public AirportDTO convertToDto(Airport entity) {
        AirportDTO dto = new AirportDTO(entity);
        dto.add(linkTo(methodOn(AirportController.class).getAirportById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(AirportController.class).getAllAirports()).withRel("airports"));
        if(entity.getCountry() != null) {
             dto.add(linkTo(methodOn(CountryController.class).getCountryById(entity.getCountry().getId())).withRel("country"));
        }
        return dto;
    }

    @GetMapping
    public CollectionModel<AirportDTO> getAllAirports() {
        List<AirportDTO> dtos = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDto).collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(AirportController.class).getAllAirports()).withSelfRel());
    }

    @GetMapping("/{id}")
    public AirportDTO getAirportById(@PathVariable Long id) {
        return convertToDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Brak ID: " + id)));
    }

    @PostMapping
    public AirportDTO addAirport(@RequestBody Airport entity) {
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public AirportDTO updateAirport(@PathVariable Long id, @RequestBody Airport updated) {
        return repository.findById(id).map(entity -> {
            entity.setIataCode(updated.getIataCode());
            entity.setName(updated.getName());
            entity.setCity(updated.getCity());
            entity.setCountry(updated.getCountry());
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Brak ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteAirport(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brak ID: " + id);
        repository.deleteById(id);
    }
}
