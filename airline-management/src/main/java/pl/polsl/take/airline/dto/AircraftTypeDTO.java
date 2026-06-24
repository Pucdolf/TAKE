package pl.polsl.take.airline.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.airline.entities.AircraftType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AircraftTypeDTO extends RepresentationModel<AircraftTypeDTO> {
    private Long id;
    private String name;
    private String description;

    public AircraftTypeDTO(AircraftType type) {
        this.id = type.getId();
        this.name = type.getName();
        this.description = type.getDescription();
    }
}
