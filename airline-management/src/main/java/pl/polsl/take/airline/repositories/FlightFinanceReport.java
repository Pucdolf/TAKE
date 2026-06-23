package pl.polsl.take.airline.repositories;

import java.math.BigDecimal;

public interface FlightFinanceReport {
    String getFlightNumber();
    String getOriginAirport();
    String getDestinationAirport();
    Integer getTicketsSold();
    BigDecimal getTotalRevenue();
}