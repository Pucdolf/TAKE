package pl.polsl.take.airline.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.airline.entities.Passenger;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerDTO extends RepresentationModel<PassengerDTO> {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String passportNumber;

    public PassengerDTO(Passenger passenger) {
        this.id = passenger.getId();
        this.firstName = passenger.getFirstName();
        this.lastName = passenger.getLastName();
        this.email = passenger.getEmail();
        this.passportNumber = passenger.getPassportNumber();
    }
}
