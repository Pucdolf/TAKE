package pl.polsl.take.airline.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "flight_statuses")
@Data
public class FlightStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String description;
}
