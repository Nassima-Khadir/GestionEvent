package org.event.gestionevenement.Repository;

import org.event.gestionevenement.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByRole(String role);
}
