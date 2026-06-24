package pl.polsl.take.airline.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.airline.entities.Country;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryDTO extends RepresentationModel<CountryDTO> {
    private Long id;
    private String name;
    private String isoCode;

    public CountryDTO(Country country) {
        this.id = country.getId();
        this.name = country.getName();
        this.isoCode = country.getIsoCode();
    }
}
