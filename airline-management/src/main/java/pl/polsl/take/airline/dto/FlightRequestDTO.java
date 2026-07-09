package pl.polsl.take.airline.dto;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class FlightRequestDTO {
    private String flightNumber;
    private Long originAirportId;
    private Long destinationAirportId;
    private Long airplaneId;
    private Long statusId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}
