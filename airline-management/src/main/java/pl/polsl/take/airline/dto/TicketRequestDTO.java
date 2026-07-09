package pl.polsl.take.airline.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class TicketRequestDTO {
    private Long flightId;
    private Long passengerId;
    private String seatClass;
    private String seatNumber;
    private BigDecimal basePrice;
    private LocalDateTime bookingTime;
}
