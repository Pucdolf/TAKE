package pl.polsl.take.airline.dto;
import lombok.Data;
@Data
public class PassengerRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String passportNumber;
}
