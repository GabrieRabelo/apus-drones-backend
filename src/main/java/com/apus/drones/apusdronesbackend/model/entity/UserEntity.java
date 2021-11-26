package com.apus.drones.apusdronesbackend.model.entity;

import com.apus.drones.apusdronesbackend.model.enums.PartnerStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String cpfCnpj;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Column
    private String avatarUrl;

    @Column
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;

    @Column
    @Builder.Default
    private PartnerStatus status = PartnerStatus.PENDING;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    @Builder.Default
    private List<ProductEntity> productEntity = new ArrayList<>();

}
