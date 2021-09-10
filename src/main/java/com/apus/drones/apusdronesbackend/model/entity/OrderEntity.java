package com.apus.drones.apusdronesbackend.model.entity;

import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    private UserEntity customer;

    @ManyToOne
    private UserEntity partner;

    @Column
    private OrderStatus status;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    @Column
    private BigDecimal deliveryPrice;

    @OneToOne
    @JoinColumn()
    private AddressEntity deliveryAddress;

    @ManyToOne
    @JoinColumn()
    private AddressEntity shopAddress;

    @Column
    private BigDecimal orderPrice;

    @Transient
    private List<OrderItemEntity> orderItems;

}