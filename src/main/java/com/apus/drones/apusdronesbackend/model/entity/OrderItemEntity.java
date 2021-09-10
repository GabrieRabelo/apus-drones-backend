package com.apus.drones.apusdronesbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "orderItems")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Integer quantity;

    @Column
    private BigDecimal price;

    @Column
    private Double weight;

    @ManyToOne
    private ProductEntity product;

    @ManyToOne
    @JsonIgnore
    private OrderEntity order;
}
