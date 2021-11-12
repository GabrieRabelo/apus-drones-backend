package com.apus.drones.apusdronesbackend.utils;

import com.apus.drones.apusdronesbackend.config.CustomUserDetails;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class PermissionsChecker {
    public static CustomUserDetails checkPermissions(List<Role> roles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();

        if (!auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
        if (roles != null && roles.size() > 0 && !roles.contains(details.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário sem permissões");
        }

        return details;
    }

    public static CustomUserDetails checkPermissions(Role role) {
        return checkPermissions(List.of(role));
    }
}
