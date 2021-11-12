package com.apus.drones.apusdronesbackend.model.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "trips")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TripEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    private UserEntity pilot;

    @OneToOne
    private OrderEntity order;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime collectedAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime deliveredAt;
}
