package pl.polsl.take.airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.airline.entities.Employee;
import pl.polsl.take.airline.repositories.EmployeeRepository;
import pl.polsl.take.airline.dto.EmployeeDTO;
import pl.polsl.take.airline.dto.EmployeeRequestDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired private EmployeeRepository repository;

    private EmployeeDTO convertToDto(Employee entity) {
        EmployeeDTO dto = new EmployeeDTO(entity);
        dto.add(linkTo(methodOn(EmployeeController.class).getEmployeeById(entity.getId())).withSelfRel());
        dto.add(linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"));
        return dto;
    }

    @GetMapping
    public CollectionModel<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> dtos = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::convertToDto).collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable Long id) {
        return convertToDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Brak ID: " + id)));
    }

    @PostMapping
    public EmployeeDTO addEmployee(@RequestBody EmployeeRequestDTO req) {
        Employee entity = new Employee();
        entity.setFirstName(req.getFirstName());
        entity.setLastName(req.getLastName());
        entity.setEmail(req.getEmail());
        entity.setJobTitle(req.getJobTitle());
        entity.setLicenseNumber(req.getLicenseNumber());
        return convertToDto(repository.save(entity));
    }

    @PutMapping("/{id}")
    public EmployeeDTO updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO req) {
        return repository.findById(id).map(entity -> {
            entity.setFirstName(req.getFirstName());
            entity.setLastName(req.getLastName());
            entity.setEmail(req.getEmail());
            entity.setJobTitle(req.getJobTitle());
            entity.setLicenseNumber(req.getLicenseNumber());
            return convertToDto(repository.save(entity));
        }).orElseThrow(() -> new RuntimeException("Brak ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brak ID: " + id);
        repository.deleteById(id);
    }
}
