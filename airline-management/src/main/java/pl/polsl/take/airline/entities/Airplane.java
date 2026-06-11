package pl.polsl.take.airline.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "airplanes")
@Data
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(name = "registration_number", unique = true, nullable = false, length = 20)
    private String registrationNumber;

    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private AircraftType type;
}
