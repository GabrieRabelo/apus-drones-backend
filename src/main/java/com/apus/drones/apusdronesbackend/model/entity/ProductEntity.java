package com.apus.drones.apusdronesbackend.model.entity;

import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long userId;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column
    private double weight;

    //@CreatedDate
    //@Temporal(value = TemporalType.TIMESTAMP)
    @Column
    private LocalDateTime createDate;
}