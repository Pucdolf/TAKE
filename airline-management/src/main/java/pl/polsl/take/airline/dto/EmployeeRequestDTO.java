package pl.polsl.take.airline.dto;
import lombok.Data;
@Data
public class EmployeeRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String jobTitle;
    private String licenseNumber;
}
