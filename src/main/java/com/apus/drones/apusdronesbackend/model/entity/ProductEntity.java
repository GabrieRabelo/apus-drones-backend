package com.apus.drones.apusdronesbackend.model.entity;

import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "product")
public class ProductEntity {

    public ProductEntity() {
    }

    public ProductEntity(String name, BigDecimal price, ProductStatus status, double weight) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.weight = weight;
        this.createDate = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

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