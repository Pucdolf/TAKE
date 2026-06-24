package pl.polsl.take.airline.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.airline.entities.Flight;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class FlightDTO extends RepresentationModel<FlightDTO> {
    private Long id;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public FlightDTO(Flight flight) {
        this.id = flight.getId();
        this.flightNumber = flight.getFlightNumber();
        this.departureTime = flight.getDepartureTime();
        this.arrivalTime = flight.getArrivalTime();
    }
}
