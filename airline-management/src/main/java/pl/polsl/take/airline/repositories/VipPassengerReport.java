package pl.polsl.take.airline.repositories;

import java.math.BigDecimal;

public interface VipPassengerReport {
    String getFirstName();
    String getLastName();
    String getPassportNumber();
    Integer getTotalFlights();
    BigDecimal getLifetimeValue();
}