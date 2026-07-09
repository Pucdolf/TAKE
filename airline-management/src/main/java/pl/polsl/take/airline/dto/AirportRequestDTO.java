package pl.polsl.take.airline.dto;
import lombok.Data;
@Data
public class AirportRequestDTO {
    private String iataCode;
    private String name;
    private String city;
    private Long countryId;
}
