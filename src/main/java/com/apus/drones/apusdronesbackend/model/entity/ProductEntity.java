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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "products")
public class ProductEntity {

    public ProductEntity(String name, BigDecimal price, ProductStatus status, double weight) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.weight = weight;
        this.createDate = LocalDateTime.now();
        this.deleted = Boolean.FALSE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    private UserEntity user;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column
    private Double weight;

    @Column Integer quantity;

    @Column
    private LocalDateTime createDate;

    @Column
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.EAGER, targetEntity = ProductImage.class, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

}