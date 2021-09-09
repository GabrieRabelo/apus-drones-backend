package com.apus.drones.apusdronesbackend.model.entity;

import com.apus.drones.apusdronesbackend.service.dto.ProductDto;
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

    @ManyToOne
    private ProductEntity product;

    @ManyToOne
    @JsonIgnore
    private OrderEntity order;
}
