package com.apus.drones.apusdronesbackend.model.entity;

import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Entity(name = "addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(columnDefinition = "POINT")
    private Point coordinates;

    @Column
    private String description;

    @Column
    private String zipCode;

    @Column
    private Integer number;

    @Column
    private String complement;

    @OneToOne
    @JoinColumn
    private UserEntity user;
}
