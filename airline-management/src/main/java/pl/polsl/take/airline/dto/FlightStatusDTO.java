package pl.polsl.take.airline.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.airline.entities.FlightStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightStatusDTO extends RepresentationModel<FlightStatusDTO> {
    private Long id;
    private String code;
    private String description;

    public FlightStatusDTO(FlightStatus status) {
        this.id = status.getId();
        this.code = status.getCode();
        this.description = status.getDescription();
    }
}
