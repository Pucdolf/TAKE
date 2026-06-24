package pl.polsl.take.airline.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.airline.entities.Airplane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirplaneDTO extends RepresentationModel<AirplaneDTO> {
    private Long id;
    private String model;
    private String registrationNumber;
    private Integer capacity;

    public AirplaneDTO(Airplane airplane) {
        this.id = airplane.getId();
        this.model = airplane.getModel();
        this.registrationNumber = airplane.getRegistrationNumber();
        this.capacity = airplane.getCapacity();
    }
}
