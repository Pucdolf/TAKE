package pl.polsl.take.airline.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.airline.entities.Employee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO extends RepresentationModel<EmployeeDTO> {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String jobTitle;
    private String licenseNumber;

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.jobTitle = employee.getJobTitle();
        this.licenseNumber = employee.getLicenseNumber();
    }
}
