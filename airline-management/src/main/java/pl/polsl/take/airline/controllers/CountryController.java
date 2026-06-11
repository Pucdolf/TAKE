package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Country;
import pl.polsl.take.airline.repositories.CountryRepository;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping
    public Iterable<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @PostMapping
    public Country addCountry(@RequestBody Country country) {
        return countryRepository.save(country);
    }
}
