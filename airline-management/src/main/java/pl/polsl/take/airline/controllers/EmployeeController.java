package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Employee;
import pl.polsl.take.airline.repositories.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    public CollectionModel<EntityModel<Employee>> getAllEmployees() {
        List<EntityModel<Employee>> employees = StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .map(employee -> EntityModel.of(employee,
                        linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel(),
                        linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees")))
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono pracownika o ID: " + id));

        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("all-employees"));
    }

    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setFirstName(updatedEmployee.getFirstName());
            employee.setLastName(updatedEmployee.getLastName());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setJobTitle(updatedEmployee.getJobTitle());
            employee.setLicenseNumber(updatedEmployee.getLicenseNumber());
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono pracownika o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Nie można usunąć. Pracownik o ID: " + id + " nie istnieje.");
        }
        employeeRepository.deleteById(id);
    }
}