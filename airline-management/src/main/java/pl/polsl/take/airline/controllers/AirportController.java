package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Airport;
import pl.polsl.take.airline.entities.Country;
import pl.polsl.take.airline.repositories.AirportRepository;
import pl.polsl.take.airline.repositories.CountryRepository;
import pl.polsl.take.airline.dto.AirportDTO;
import pl.polsl.take.airline.dto.AirportRequestDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/airports")
public class AirportController {
    @Autowired private AirportRepository repository;
    @Autowired private CountryRepository countryRepository;

    public AirportDTO convertToDto(Airport entity) {
        AirportDTO dto = new AirportDTO(entity);
        dto.add(linkTo(methodOn(AirportController.class).getAirportById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(AirportController.class).getAllAirports()).withRel("airports"));
        if (entity.getCountry() != null) {
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
    public AirportDTO addAirport(@RequestBody AirportRequestDTO req) {
        Airport entity = new Airport();
        entity.setIataCode(req.getIataCode());
        entity.setName(req.getName());
        entity.setCity(req.getCity());
        if (req.getCountryId() != null) {
            Country country = countryRepository.findById(req.getCountryId()).orElseThrow(() -> new RuntimeException("Brak Country ID"));
            entity.setCountry(country);
        }
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public AirportDTO updateAirport(@PathVariable Long id, @RequestBody AirportRequestDTO req) {
        return repository.findById(id).map(entity -> {
            entity.setIataCode(req.getIataCode());
            entity.setName(req.getName());
            entity.setCity(req.getCity());
            if (req.getCountryId() != null) {
                Country country = countryRepository.findById(req.getCountryId()).orElseThrow(() -> new RuntimeException("Brak Country ID"));
                entity.setCountry(country);
            } else {
                entity.setCountry(null);
            }
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Brak ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteAirport(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brak ID: " + id);
        repository.deleteById(id);
    }
}
