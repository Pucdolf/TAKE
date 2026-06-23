package pl.polsl.take.airline.repositories;

public interface FlightLoadFactorReport {
    String getFlightNumber();
    String getAirplaneModel();
    Integer getTotalSeats();
    Integer getSeatsTaken();
    Double getLoadFactorPercentage();
}