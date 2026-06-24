package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Country;
import pl.polsl.take.airline.repositories.CountryRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping
    public CollectionModel<EntityModel<Country>> getAllCountries() {
        List<EntityModel<Country>> countries = StreamSupport.stream(countryRepository.findAll().spliterator(), false)
                .map(country -> EntityModel.of(country,
                        linkTo(methodOn(CountryController.class).getCountryById(country.getId())).withSelfRel(),
                        linkTo(methodOn(CountryController.class).getAllCountries()).withRel("countries")))
                .collect(Collectors.toList());

        return CollectionModel.of(countries, linkTo(methodOn(CountryController.class).getAllCountries()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Country> getCountryById(@PathVariable Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono kraju o ID: " + id));

        return EntityModel.of(country,
                linkTo(methodOn(CountryController.class).getCountryById(id)).withSelfRel(),
                linkTo(methodOn(CountryController.class).getAllCountries()).withRel("all-countries"));
    }

    @PostMapping
    public Country addCountry(@RequestBody Country country) {
        return countryRepository.save(country);
    }

    @PutMapping("/{id}")
    public Country updateCountry(@PathVariable Long id, @RequestBody Country updatedCountry) {
        return countryRepository.findById(id).map(country -> {
            country.setIsoCode(updatedCountry.getIsoCode());
            country.setName(updatedCountry.getName());
            return countryRepository.save(country);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono kraju o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable Long id) {
        if (!countryRepository.existsById(id)) {
            throw new RuntimeException("Nie można usunąć. Kraj o ID: " + id + " nie istnieje.");
        }
        countryRepository.deleteById(id);
    }
}