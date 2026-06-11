package pl.polsl.take.airline.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "countries")
@Data
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iso_code", unique = true, nullable = false, length = 2)
    private String isoCode;

    @Column(nullable = false, length = 100)
    private String name;
}
