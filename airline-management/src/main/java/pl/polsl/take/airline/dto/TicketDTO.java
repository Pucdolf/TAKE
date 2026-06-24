package pl.polsl.take.airline.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.airline.entities.Ticket;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TicketDTO extends RepresentationModel<TicketDTO> {
    private Long id;
    private String seatClass;
    private String seatNumber;
    private BigDecimal basePrice;
    private LocalDateTime bookingTime;

    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.seatClass = ticket.getSeatClass();
        this.seatNumber = ticket.getSeatNumber();
        this.basePrice = ticket.getBasePrice();
        this.bookingTime = ticket.getBookingTime();
    }
}
