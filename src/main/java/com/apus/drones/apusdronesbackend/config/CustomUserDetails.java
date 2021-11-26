package com.apus.drones.apusdronesbackend.config;

import com.apus.drones.apusdronesbackend.model.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {

    private final Long userID;
    private final Role role;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                             Long userID, Role role) {
        super(username, password, authorities);
        this.userID = userID;
        this.role = role;
    }
}
