package pl.polsl.take.airline.dto;
import lombok.Data;
@Data
public class AirplaneRequestDTO {
    private String model;
    private String registrationNumber;
    private Integer capacity;
    private Long typeId;
}
