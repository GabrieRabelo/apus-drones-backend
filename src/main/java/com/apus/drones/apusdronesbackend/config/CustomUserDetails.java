package com.apus.drones.apusdronesbackend.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {

        private final Long userID;

        public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userID) {
            super(username, password, authorities);
            this.userID = userID;
        }
}
