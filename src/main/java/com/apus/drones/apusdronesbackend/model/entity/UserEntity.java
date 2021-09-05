package com.apus.drones.apusdronesbackend.model.entity;

import com.apus.drones.apusdronesbackend.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}
