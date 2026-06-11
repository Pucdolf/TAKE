package pl.polsl.take.airline.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "airports")
@Data
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iata_code", unique = true, nullable = false, length = 3)
    private String iataCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
}
