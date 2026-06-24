package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Country;
import pl.polsl.take.airline.repositories.CountryRepository;
import pl.polsl.take.airline.dto.CountryDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    private CountryRepository repository;

    private CountryDTO convertToDto(Country entity) {
        CountryDTO dto = new CountryDTO(entity);
        dto.add(linkTo(methodOn(CountryController.class).getCountryById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(CountryController.class).getAllCountries()).withRel("countries"));
        return dto;
    }

    @GetMapping
    public CollectionModel<CountryDTO> getAllCountries() {
        List<CountryDTO> dtos = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(CountryController.class).getAllCountries()).withSelfRel());
    }

    @GetMapping("/{id}")
    public CountryDTO getCountryById(@PathVariable Long id) {
        Country entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono kraju o ID: " + id));
        return convertToDto(entity);
    }

    @PostMapping
    public CountryDTO addCountry(@RequestBody Country entity) {
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public CountryDTO updateCountry(@PathVariable Long id, @RequestBody Country updatedEntity) {
        return repository.findById(id).map(entity -> {
            entity.setName(updatedEntity.getName());
            entity.setIsoCode(updatedEntity.getIsoCode());
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono kraju o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Nie można usunąć. Kraj o ID: " + id + " nie istnieje.");
        repository.deleteById(id);
    }
}
