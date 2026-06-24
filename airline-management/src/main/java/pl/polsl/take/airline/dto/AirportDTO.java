package pl.polsl.take.airline.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.airline.entities.Airport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirportDTO extends RepresentationModel<AirportDTO> {
    private Long id;
    private String iataCode;
    private String name;
    private String city;

    public AirportDTO(Airport airport) {
        this.id = airport.getId();
        this.iataCode = airport.getIataCode();
        this.name = airport.getName();
        this.city = airport.getCity();
    }
}
