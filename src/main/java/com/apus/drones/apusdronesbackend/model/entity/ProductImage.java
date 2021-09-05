package com.apus.drones.apusdronesbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
@Entity
public class ProductImage {
    @Id
    private Long id;
    private String url;
    private boolean isMain;
    @JsonIgnore
    @ManyToOne
    private ProductEntity product;
}
